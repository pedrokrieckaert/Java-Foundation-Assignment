package src;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import com.google.gson.Gson;

import com.google.gson.JsonObject;
import src.data.pojo.CartItem;
import src.data.pojo.Product;
import src.data.repository.CartItemRepo;
import src.data.repository.OpeningHoursRepo;
import src.data.repository.ProductRepo;
import src.service.CartItemService;
import src.service.OpeningHoursService;
import src.service.ProductService;

import static src.data.reader.JSONReader.readOrder;
import static src.data.reader.JSONReader.saveOrder;

class Main {
    static ProductRepo productRepo = new ProductRepo();
    static ProductService productService = new ProductService(productRepo);
    static OpeningHoursRepo openingHoursRepo = new OpeningHoursRepo();
    static OpeningHoursService openingHoursService = new OpeningHoursService(openingHoursRepo);
    static CartItemRepo cartItemRepo = new CartItemRepo();
    static CartItemService cartItemService = new CartItemService(cartItemRepo);
    static String shoppingCart = "database/shoppingCart.json";

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        openingHoursRepo.load();
        productRepo.load();

//        Product test = promptForProduct(scan);
//
//        int productQuantity = 4;
//
//        CartItem item = new CartItem(test, productQuantity);
//
//        cartItemService.addCartItem(item);
//
//        System.out.println(cartItemService.getCartItem(1));
//
//        JsonObject orderSum = new JsonObject();
//        BigDecimal totalPrice = cartItemService.getCartItem(1).getPrice().multiply(BigDecimal.valueOf(cartItemService.getCartItem(1).getAmount()));
//        orderSum.addProperty("totalPrice", totalPrice);
//        int totalHours = cartItemService.getCartItem(1).getHours() * cartItemService.getCartItem(1).getAmount();
//        orderSum.addProperty("totalHours", totalHours);
//
//        Gson gson = new Gson();
//
//        JsonObject order = new JsonObject();
//        order.add("summary", orderSum);
//
//
//        try {
//            List<CartItem> cart = readOrder(shoppingCart);
//            cart.add(item);
//            order.add("cart", gson.toJsonTree(cart));
//
//            System.out.println(order);
//            saveOrder(order, shoppingCart);
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//        }
//
//        scan.close();
    }

    /**
     * Check if scanned line has input
     * @param input String
     * @return boolean
     */
    public static boolean isNullOrBlank(String input) {
        return input == null || input.isBlank();
    }

    /**
     * Check if scanned int is within src.data.repository bounds
     * @param index int
     * @return boolean
     */
    public static boolean invalidIndex(int index) {
        return index < 0 || index > 11;
    }

    /**
     * CLI prompt to retrieve a product from the src.data.repository by id or Name
     * @param scan Scanner
     * @return Product
     */
    public static Product promptForProduct(Scanner scan) {
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
}
