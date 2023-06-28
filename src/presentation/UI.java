package src.presentation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import src.data.pojo.CartItem;
import src.data.pojo.Order;
import src.data.pojo.Product;
import src.data.repository.OpeningHoursRepo;
import src.data.repository.ProductRepo;
import src.service.CartItemService;
import src.service.OpeningHoursService;
import src.service.ProductService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static src.data.reader.JSONReader.readOrder;
import static src.data.reader.JSONReader.saveOrder;
import static src.validation.Validation.invalidIndex;
import static src.validation.Validation.isNullOrBlank;

public class UI {
    static ProductService productService = new ProductService();
    static OpeningHoursService openingHoursService = new OpeningHoursService();
    static CartItemService cartItemService = new CartItemService();
    static String shoppingCart = "database/shoppingCart.json";

    /**
     * CLI prompt to retrieve a product from the src.data.repository by id or Name
     * @return Product
     */
    public static Product promptForProduct() {
        Scanner scan = new Scanner(System.in);
        System.out.print("\nPlease enter the name or id of a product: ");
        while (true) {
            //Check if the input is an integer
            if(scan.hasNextInt()) {
                int index = scan.nextInt();

                //Validate if the input is within src.data.repository bounds
                if (!invalidIndex(index)) {
                    return productService.retrieveProductById(index);
                } else {
                    System.out.println("Id does not exist, please select a valid ID.");
                    scan.nextLine();
                }

                //If the input was a String
            } else {
                String input = scan.nextLine();
                //Validate if the input is null or blank
                if (isNullOrBlank(input)) {
                    scan.skip("");
                    continue;
                }

                Product temp = productService.retrieveProductByName(input);

                //If the return from src.service was null, means that the input wasn't found in the product names
                if (temp == null) {
                    System.out.println("There is no product by that name.");
                } else {
                    //Return the product object once validated
                    return temp;
                }
            }
        }
    }

    public static void testProcess(){
        Scanner scan = new Scanner(System.in);

        Product test = promptForProduct();

        int productQuantity = 4;

        CartItem item = new CartItem(test, productQuantity);

        cartItemService.addCartItem(item);

        System.out.println(cartItemService.getCartItem(1));

        JsonObject orderSum = new JsonObject();
        BigDecimal totalPrice = cartItemService.getCartItem(1).getPrice().multiply(BigDecimal.valueOf(cartItemService.getCartItem(1).getAmount()));
        orderSum.addProperty("totalPrice", totalPrice);
        int totalHours = cartItemService.getCartItem(1).getHoursInt() * cartItemService.getCartItem(1).getAmount();
        orderSum.addProperty("totalHours", totalHours);

        Gson gson = new Gson();

        JsonObject newOrder = new JsonObject();
        newOrder.add("summary", orderSum);


        try {
            Order order = readOrder(shoppingCart);
            List<CartItem> cart = new ArrayList<>();
            if (!(order == null)) {
                cart = order.getCart();
            }
            cart.add(item);
            newOrder.add("cart", gson.toJsonTree(cart));

            System.out.println(order);
            saveOrder(newOrder, shoppingCart);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
