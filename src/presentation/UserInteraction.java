package src.presentation;

import src.pojo.CartItem;

import java.util.List;
import java.util.Scanner;

import static src.presentation.UserInput.*;
import static src.utilities.Validation.isNullOrBlank;

abstract class UserInteraction {
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
    static int promptForQuantity(String message) {
        Scanner scan = new Scanner(System.in);
        System.out.print(message + " ");

        return scanInt(scan, 1, CartItem.MAX_AMOUNT);
    }
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

    static void promptContinue() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Press 'Enter' to continue...");

        try {
            scan.nextLine();
        } catch (Exception e) { }
    }

    static boolean promptBinaryChoice() {
        return promptBinaryChoice("\nDo you want to continue?");
    }
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
