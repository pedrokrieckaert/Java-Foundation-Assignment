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
import java.util.concurrent.*;

import static src.data.reader.JSONReader.saveOrder;
import static src.presentation.Interaction.*;
import static src.presentation.Invoice.*;
import static src.presentation.DataDisplay.*;

public class UI {
    static ProductService productService = new ProductService();
    static OpeningHoursService openingHoursService = new OpeningHoursService();
    static CartItemService cartItemService = new CartItemService();
    static OrderService orderService = new OrderService();
    static final String SHOPPING_CART = "database/shoppingCart.json";

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
        System.out.println("To the Photo Shop.\n");
        promptContinue();

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
        processRequestLoop("\nWould you like to add another item?", () -> {
            Object input = promptForProduct();

            if (input instanceof String) {
                if (input.toString().equalsIgnoreCase("cancel")) {
                    return true;
                }
            }

            Product product = fetchProduct(input);

            while (product == null) {
                System.out.print("\nSorry, that product doesn't exist. ");
                product = fetchProduct(promptForProduct());
            }

            CartItem item = new CartItem(product, promptForQuantity("Please select quantity:"));

            try {
                int currentAmount = cartItemService.getCartItem(item.getId()).getAmount();

                if (currentAmount == CartItem.MAX_AMOUNT) {
                    System.out.println("Item [" + item.getName() + "] already at maximum amount: " + CartItem.MAX_AMOUNT);
                } else {
                    cartItemService.addCartItem(item);
                    System.out.println("Added item [" + item.getName() + "] x" + item.getAmount());
                }
            } catch (NullPointerException e) {
                cartItemService.addCartItem(item);
                System.out.println("Added item [" + item.getName() + "] x" + item.getAmount());
            }

            return false;
        });
    }

    private static void processCartDisplay() {
            List<CartItem> cart = cartItemService.getCart();

            if(cart.size() == 0) {
                System.out.println("Your cart is empty.");

                promptContinue();
            } else {
                displayCart(cart, cartItemService.calcTotalPrice().toString());
                processCartFacade();
            }
    }

    private static void processCartFacade(){
        ProcessCartActions action;

        while (true) {
            action = promptForAction(ProcessCartActions.values());

            switch (action) {
                case EDIT -> processEditItem();
                case REMOVE -> {
                    boolean exit = processRemoveItem();

                    if (exit) {
                        return;
                    }
                }
                case DONE -> {
                    return;
                }
            }
        }
    }
    private static void processEditItem() {
        List<CartItem> bufferCart = cartItemService.getCart();

        processRequestLoop("\nEdit another item?: ", () -> {
            int itemIndex = promptForCartItem(bufferCart);

            if (itemIndex == Integer.MIN_VALUE) {
                return true;
            }

            int oldQ = bufferCart.get(itemIndex).getAmount();
            int newQ = promptForQuantity("Select new quantity:");

            CartItem itemEdit = new CartItem(bufferCart.get(itemIndex), newQ);

            bufferCart.set(itemIndex, itemEdit);

            System.out.println("Updated [" + itemEdit.getName() + "] quantity: " + oldQ + " -> " + newQ);

            return false;
        });

        cartItemService.updateCart(bufferCart);
    }

    private static boolean processRemoveItem() {
        List<CartItem> bufferCart = cartItemService.getCart();

        processRequestLoop("\nRemove another item?", () -> {
            int itemIndex = promptForCartItem(bufferCart);

            if (itemIndex == Integer.MIN_VALUE) {
                return true;
            }

            String name = bufferCart.get(itemIndex).getName();

            if (promptBinaryChoice("Are you sure you want to remove " + name + "?")) {
                System.out.println("Removed [" + name + "]");
                bufferCart.remove(itemIndex);
            }

            if (bufferCart.size() == 0) {
                System.out.println("\nYour cart is now empty.");
                promptContinue();
                return true;
            }

            return false;
        });

        cartItemService.updateCart(bufferCart);

        return bufferCart.size() == 0;
    }

    private static void processFinalize(){
        List<CartItem> cart = cartItemService.getCart();
        if (cart.size() == 0) {
            System.out.println("Your cart is empty.");
            promptContinue();
            return;
        }

        if (!promptBinaryChoice("Are you sure you want to checkout?")) {
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
            saveOrder(orderService.retrieveBufferOrder(), SHOPPING_CART);
        } catch (IOException e) {
            e.printStackTrace();
        }

        printUserData();
        printPickUp(orderService.retrieveBufferOrder());
        printCart(cart, totalPrice.toString());

        System.exit(0);
    }

    private static void processTerminate(){
        boolean end = promptBinaryChoice("Are you sure you would like to cancel?");

        if(end) {
            System.exit(0);
        }
    }

    private static void processRequestLoop(String message, Callable function){
        boolean end = false;
        ExecutorService service = Executors.newSingleThreadExecutor();

        while (!end) {
            Future<Boolean> exit = service.submit(function);

            try {
                end = exit.get();
                if (!end) {
                    end = !promptBinaryChoice(message);
                }
            } catch (Exception e) {
                end = !promptBinaryChoice(message);
            }
        }
    }
}
