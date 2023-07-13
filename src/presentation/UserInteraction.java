package src.presentation;

import src.pojo.CartItem;

import java.util.List;
import java.util.Scanner;

import static src.presentation.UserInput.*;
import static src.utilities.Validation.isNullOrBlank;

abstract class UserInteraction {

    /**
     * Prints the values of an Enum interfaced by ActionEnum and prompts to select on of the values.
     * @param actions Array of Enum values
     * @return The selected Enum value
     */
    static <T extends Enum<T> & ActionEnum> T promptForAction(T[] actions) {
        Scanner scan = new Scanner(System.in);

        System.out.println("\nWhat would you like to do?");

        for (T action : actions) {
            System.out.println(action.toString());
        }

        while (true) {
            System.out.print("Select index or name: ");
            Object input = scanIntOrString(scan, 1, actions.length);

            if (input instanceof Integer) {
                int index = Integer.parseInt(String.valueOf(input));

                for (T action : actions) {
                    if (action.getInt() == index) {
                        return action;
                    }
                }
            } else if (input instanceof String) {
                String name = input.toString();

                for (T action : actions) {
                    if (action.getString().equalsIgnoreCase(name)) {
                        return action;
                    }
                }

                System.out.print("\nNo action by the name: '" + name + "'.");

            }
        }
    }

    /**
     * Prompts to select a product by the product's ID or name.
     * @return Generic int or String identifier of the product
     */
    static <T> T promptForProduct(){
        Scanner scan = new Scanner(System.in);

        System.out.print("\nSelect product by id/name, or type 'cancel' to return: ");

        while(true) {
            Object input = scanIntOrString(scan, 1, 12);

            if (input instanceof Integer) {
                int index = Integer.parseInt(String.valueOf(input));
                return (T) Integer.valueOf(index);
            } else if (input instanceof String) {
                String name = input.toString();
                return (T) name;
            }
        }
    }

    /**
     * Prompts to select a quantity for a product.
     * @param message This prompt
     * @return int amount of a product
     */
    static int promptForQuantity(String message) {
        Scanner scan = new Scanner(System.in);
        System.out.print(message + " ");

        return scanInt(scan, 1, CartItem.MAX_AMOUNT);
    }

    /**
     * Prompts to select a product, from the cart, by the product's index in the cart or name.
     * @param cart List of CartItem
     * @return int Index of the selected product
     */
    static int promptForCartItem(List<CartItem> cart) {
        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.print("\nSelect product by index/name, or type 'cancel' to return: ");
            Object input = scanIntOrString(scan, 1, cart.size());

            if (input instanceof Integer) {
                int index = Integer.parseInt(String.valueOf(input));

                return index - 1;
            } else if (input instanceof String) {

                if (input.toString().equalsIgnoreCase("cancel")) {
                    return Integer.MIN_VALUE;
                }

                for (int i = 0; i < cart.size(); i++) {
                    if (cart.get(i).getName().equalsIgnoreCase(input.toString())) {
                        return i;
                    }
                }
            }
        }
    }

    /**
     * Prompts for an 'Enter' input, pausing the program.
     */
    static void promptContinue() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Press 'Enter' to continue...");

        //try-catch block to accept an empty line
        try {
            scan.nextLine();
        } catch (Exception ignored) { }
    }

    /**
     * Prompts for a binary choice, yes or no, scanning for a string input of either 'y' or 'n'.
     * @param prompt This prompt message
     * @return boolean 'y' returns true, 'n' returns false
     */
    static boolean promptBinaryChoice(String prompt){
        Scanner scan = new Scanner(System.in);
        System.out.print(prompt + " (y/n): ");

        while (true) {
            String input = scan.nextLine();
            if (isNullOrBlank(input)) {
                scan.skip("");
                continue;
            }

            if (!input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n")) {
                System.out.print("\nPlease provide a valid input. Do you want to continue? (y/n): ");
                continue;
            } else {
                switch (input.toLowerCase()) {
                    case "y" -> {
                        return true;
                    }
                    case "n" -> {
                        return false;
                    }
                }
            }
        }
    }
}
