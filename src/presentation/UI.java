package src.presentation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import src.data.pojo.CartItem;
import src.data.pojo.Product;
import src.service.CartItemService;
import src.service.OpeningHoursService;
import src.service.OrderService;
import src.service.ProductService;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static src.data.reader.JSONReader.saveOrder;
import static src.presentation.Interaction.*;
import static src.presentation.Invoice.*;
import static src.presentation.DataDisplay.*;

public class UI {
    static ProductService productService = new ProductService();
    static OpeningHoursService openingHoursService = new OpeningHoursService();
    static CartItemService cartItemService = new CartItemService();
    static OrderService orderService = new OrderService();
    static String shoppingCart = "database/shoppingCart.json";

    private static void finalizeOrder() {
        Gson gson = new Gson();
        JsonObject orderSum = new JsonObject();
        JsonObject jsonOrder = new JsonObject();
        List<CartItem> cart = cartItemService.getCart();

        //Total Price
        BigDecimal totalPrice = cartItemService.calcTotalPrice();
        orderSum.addProperty("totalPrice", totalPrice);

        //Total Hours
        int totalHours = cartItemService.calcTotalHours();
        orderSum.addProperty("totalProductionHours", totalHours);

        orderService.createNewOrder(totalPrice, totalHours, cart);

        orderSum.addProperty("pickUp", orderService.calcPickUpWindow(openingHoursService.retrieveOpeningHoursList()));

        jsonOrder.add("summary", orderSum);

        jsonOrder.add("cart", gson.toJsonTree(orderService.retrieveBufferOrder().getCart()));


        try {
            saveOrder(jsonOrder, shoppingCart);
        } catch (IOException e) {
            e.printStackTrace();
        }

        printUserData();
        printPickUp(orderService.retrieveBufferOrder());
        printCart(cart, totalPrice.toString());
    }

    public static void processFacade(){
        ProcessFacadeActions action;
        System.out.println(
                " /$$      /$$           /$$                                            \n" +
                "| $$  /$ | $$          | $$                                            \n" +
                "| $$ /$$$| $$  /$$$$$$ | $$  /$$$$$$$  /$$$$$$  /$$$$$$/$$$$   /$$$$$$ \n" +
                "| $$/$$ $$ $$ /$$__  $$| $$ /$$_____/ /$$__  $$| $$_  $$_  $$ /$$__  $$\n" +
                "| $$$$_  $$$$| $$$$$$$$| $$| $$      | $$  \\ $$| $$ \\ $$ \\ $$| $$$$$$$$\n" +
                "| $$$/ \\  $$$| $$_____/| $$| $$      | $$  | $$| $$ | $$ | $$| $$_____/\n" +
                "| $$/   \\  $$|  $$$$$$$| $$|  $$$$$$$|  $$$$$$/| $$ | $$ | $$|  $$$$$$$\n" +
                "|__/     \\__/ \\_______/|__/ \\_______/ \\______/ |__/ |__/ |__/ \\_______/"
        );
        System.out.println("To the Photo Shop.");

        while (true) {
            action = promptForAction(ProcessFacadeActions.values());

            switch (action) {
                case ADD -> processAddItem();
                case PRODUCTS -> displayProducts(productService.retrieveProductList());
                case HOURS -> displayHours(openingHoursService.retrieveOpeningHoursList());
                case CART -> processCartDisplay();
                case FINALIZE -> processFinalize();
                case TERMINATE -> processTerminate();
            }
        }
    }

    private static <T> Product fetchProduct(T input){
        return productService.retrieveProduct(input);
    }

    private static void processAddItem(){
        CartItem item = null;
        boolean end = true;

        while (end) {
            Product product = fetchProduct(promptForProduct());

            while (product == null) {
                System.out.print("\nSorry, that product doesn't exist. ");
                product = fetchProduct(promptForProduct());
            }

            item = promptForQuantity(product);
            cartItemService.addCartItem(item);
            end = promptEndProcess("Would you like to add another item?");
        }
    }

    private static void processCartDisplay() {
            List<CartItem> cart = cartItemService.getCart();

            if(cart.size() == 0) {
                System.out.println("Your cart is empty.");

                promptContinue();
            } else {
                printCart(cart, cartItemService.calcTotalPrice().toString());
                processCartFacade(cart);
            }
    }

    private static void processCartFacade(List<CartItem> cart){
        ProcessCartActions action;

        System.out.println("\nWhat would you like to do?");

        while (true) {
            action = promptForAction(ProcessCartActions.values());

            switch (action) {
                case EDIT -> processEditItem();
                case REMOVE -> processRemoveItem();
                case DONE -> {
                    return;
                }
            }
        }
    }
    private static void processEditItem() {
        List<CartItem> bufferCart = cartItemService.getCart();

        processRequestLoop("Edit another item?: ", () -> {
            int itemIndex = promptForCartItem(bufferCart);

            CartItem itemEdit = new CartItem(bufferCart.get(itemIndex), promptForEditQuantity());

            bufferCart.set(itemIndex, itemEdit);
        });

        cartItemService.updateCart(bufferCart);
    }

    private static void processRemoveItem() {
        List<CartItem> bufferCart = cartItemService.getCart();

        processRequestLoop("Remove another item?: ", () -> {
            int itemIndex = promptForCartItem(bufferCart);

            if (promptEndProcess("Are you sure you want to remove " + bufferCart.get(itemIndex).getName() + "?")) {
                bufferCart.remove(itemIndex);
            }
        });

        cartItemService.updateCart(bufferCart);
    }

    private static void processFinalize(){
        List<CartItem> cart = cartItemService.getCart();
        if (cart.size() == 0) {
            System.out.println("Your cart is empty.");
            promptContinue();
            return;
        }

        if (!promptEndProcess("Are you sure you want to checkout?")) {
            return;
        }

        Gson gson = new Gson();
        JsonObject orderSum = new JsonObject();
        JsonObject jsonOrder = new JsonObject();

        //Total Price
        BigDecimal totalPrice = cartItemService.calcTotalPrice();
        orderSum.addProperty("totalPrice", totalPrice);

        //Total Hours
        int totalHours = cartItemService.calcTotalHours();
        orderSum.addProperty("totalProductionHours", totalHours);

        orderService.createNewOrder(totalPrice, totalHours, cart);

        orderSum.addProperty("pickUp", orderService.calcPickUpWindow(openingHoursService.retrieveOpeningHoursList()));

        jsonOrder.add("summary", orderSum);

        jsonOrder.add("cart", gson.toJsonTree(orderService.retrieveBufferOrder().getCart()));


        try {
            saveOrder(jsonOrder, shoppingCart);
        } catch (IOException e) {
            e.printStackTrace();
        }

        printUserData();
        printPickUp(orderService.retrieveBufferOrder());
        printCart(cart, totalPrice.toString());

        System.exit(0);
    }

    private static void processTerminate(){
        boolean end = promptEndProcess("Are you sure you would like to cancel?");

        if(end) {
            System.exit(0);
        }
    }

    private static void processRequestLoop(String message ,Runnable function) {
        boolean end = true;

        while (end) {
            function.run();

            end = promptEndProcess(message);
        }
    }
}
