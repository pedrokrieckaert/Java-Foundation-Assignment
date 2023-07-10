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
import java.util.Scanner;

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
        ProcessActions action;
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
            action = promptForAction();

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
        boolean end = true;

        System.out.println("\nWhat would you like to do?");

        while (end) {
            action = promptCartAction();

            switch (action) {
                case EDIT -> processEditItem(cart);
                case REMOVE -> processRemoveItem(cart);
                case DONE -> {
                    return;
                }
            }

            end = promptEndProcess();
        }
    }
    private static void processEditItem(List<CartItem> cart) {
        List<CartItem> bufferCart = cartItemService.getCart();
        int itemIndex = promptForCartItem(bufferCart);
        CartItem itemEdit = new CartItem(cart.get(itemIndex), promptForEditQuantity());

        bufferCart.set(itemIndex, itemEdit);
        System.out.println(bufferCart);
    }

    private static void processRemoveItem(List<CartItem> cart) {

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
}
