package src.presentation;

import src.pojo.CartItem;
import src.pojo.Product;
import src.service.CartItemService;
import src.service.OpeningHoursService;
import src.service.OrderService;
import src.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.*;

import static src.presentation.UserInteraction.*;
import static src.presentation.InvoiceDisplay.*;
import static src.presentation.DataDisplay.*;

public class UserInterface {
    static ProductService productService = new ProductService();
    static OpeningHoursService openingHoursService = new OpeningHoursService();
    static CartItemService cartItemService = new CartItemService();
    static OrderService orderService = new OrderService();
    static final String SHOPPING_CART = "database/shoppingCart.json";

    /**
     * Master loop of the program. Prompts for an ActionEnum choice.
     */
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

    /**
     * Requests the service layer for a Product to return.
     * @param input Generic identifier of a product
     * @return Product
     */
    private static <T> Product fetchProduct(T input){
        return productService.retrieveProduct(input);
    }

    /**
     * Series of prompts and operations to correctly add an item to the cart.
     * Function can be canceled.
     */
    private static void processAddItem(){
        processRequestLoop("\nWould you like to add another item?", () -> {
            Object input = promptForProduct();

            if (input instanceof String) {
                if (input.toString().equalsIgnoreCase("cancel")) {
                    return true; //Break loop
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

            return false; //Continue loop
        });
    }

    /**
     * Prints the current content of the cart. If there is no cart, print a default message.
     */
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

    /**
     * Prompts for an ActionEnum choice for the cart. Contains exit logic if the cart becomes empty.
     */
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

    /**
     * Series of prompts and operations to change the amount of a product in the cart.
     * A temporary buffer cart is created to store the changes.
     * Once the process is finalized, the changes from the buffer cart are applied to the cart.
     * Function, and changes, can be canceled.
     */
    private static void processEditItem() {
        List<CartItem> bufferCart = cartItemService.getCart();

        processRequestLoop("\nEdit another item?: ", () -> {
            int itemIndex = promptForCartItem(bufferCart);

            //Since the prompt method can only return an int, an 'absurd' value is returned to validate the cancel
            if (itemIndex == Integer.MIN_VALUE) {
                return true; //Break loop
            }

            int oldQ = bufferCart.get(itemIndex).getAmount();
            int newQ = promptForQuantity("Select new quantity:");

            CartItem itemEdit = new CartItem(bufferCart.get(itemIndex), newQ);

            bufferCart.set(itemIndex, itemEdit);

            System.out.println("Updated [" + itemEdit.getName() + "] quantity: " + oldQ + " -> " + newQ);

            return false; //Continue loop
        });

        cartItemService.updateCart(bufferCart);
    }

    /**
     * Series of prompts and operations to remove an item from the cart.
     * If the cart becomes empty or the operation is canceled, this function will end.
     * @return boolean To break from the action request loop
     */
    private static boolean processRemoveItem() {
        List<CartItem> bufferCart = cartItemService.getCart();

        processRequestLoop("\nRemove another item?", () -> {
            int itemIndex = promptForCartItem(bufferCart);

            if (itemIndex == Integer.MIN_VALUE) {
                return true; //Break loop
            }

            String name = bufferCart.get(itemIndex).getName();

            if (promptBinaryChoice("Are you sure you want to remove " + name + "?")) {
                System.out.println("Removed [" + name + "]");
                bufferCart.remove(itemIndex);
            }

            if (bufferCart.size() == 0) {
                System.out.println("\nYour cart is now empty.");
                promptContinue();
                return true; //Break loop
            }

            return false; //Continue loop
        });

        cartItemService.updateCart(bufferCart);

        return bufferCart.size() == 0;
    }

    /**
     * Calculates the total price, total hours, and pick up time of the order.
     * Then stores this into an Order object, before writing it to a JSON and printing the order invoice.
     * <b>The program will exit.</b>
     */
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

        //Total Price
        BigDecimal totalPrice = cartItemService.calcTotalPrice();

        //Total Hours
        int totalHours = cartItemService.calcTotalHours();

        orderService.createNewOrder(totalPrice, totalHours, cart);

        orderService.calcPickUpWindow(openingHoursService.retrieveOpeningHoursList());

        orderService.writeOrder(SHOPPING_CART);

        printUserData();
        printPickUp(orderService.retrieveBufferOrder());
        printCart(cart, totalPrice.toString());

        System.exit(0);
    }

    /**
     * Exits the program after a security prompt.
     */
    private static void processTerminate(){
        boolean end = promptBinaryChoice("Are you sure you would like to cancel?");

        if(end) {
            System.exit(0);
        }
    }

    /**
     * A custom while loop that will prompt to be concluded and otherwise continue.
     * A lambda function parameter declares the code to be executed during the loop.
     * As a Callable, it returns a boolean to terminate the loop from within the lambda function.
     * @param message String to be displayed for the binary prompt to conclude the loop
     * @param function Callable lambda that returns a boolean
     */
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
