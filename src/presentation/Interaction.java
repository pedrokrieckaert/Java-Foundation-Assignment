package src.presentation;

import src.data.pojo.CartItem;
import src.data.pojo.Product;

import java.util.List;
import java.util.Scanner;

import static src.presentation.UserInput.*;
import static src.validation.Validation.invalidIndex;
import static src.validation.Validation.isNullOrBlank;

abstract class Interaction {
    static <T extends Enum<T> & ActionEnum> T promptForAction(T[] actions) {
        Scanner scan = new Scanner(System.in);

        for (T action : actions) {
            System.out.println(action.toString());
        }

        while (true) {
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

        System.out.print("Please enter the name or id of a product: ");

        while(true) {
            if (scan.hasNextInt()) {
                int index = scan.nextInt();

                //Validate if the input is within src.data.repository bounds
                if (!invalidIndex(index, 0, 11)) {
                    return (T) Integer.valueOf(index);
                } else {
                    System.out.print("\nInvalid id, please select a valid id (0 - 11): ");
                    scan.nextLine(); //Next line trap
                }
            } else {
                String input = scan.nextLine();

                //Validate if the input is null or blank
                if (isNullOrBlank(input)) {
                    scan.skip("");
                    continue;
                }

                return (T) input;
            }
        }
    }

    static CartItem promptForQuantity(Product product) {
        Scanner scan = new Scanner(System.in);
        System.out.print("\nPlease select the quantity: ");
        int quantity = 0;

        while (true) {
            if (scan.hasNextInt()) {
                quantity = scan.nextInt();
                if (invalidIndex(quantity, 1, 9999)) {
                    System.out.print("\nOut of range.\nPlease input a number within range (1 - 9999): ");
                    continue;
                }
            } else {
                System.out.print("\nNot a Number.\nPlease input a number within range (1 - 9999): ");
                scan.next();
                continue;
            }

            return new CartItem(product, quantity);
        }

    }

    static int promptForCartItem(List<CartItem> cart) {
        Scanner scan = new Scanner(System.in);

        while (true) {
            Object input = scanIntOrString(scan, 0, cart.size() - 1);

            if (input instanceof Integer) {
                int index = Integer.parseInt(String.valueOf(input));

                return index;
            } else if (input instanceof String) {
                for (int i = 0; i < cart.size(); i++) {
                    if (cart.get(i).getName().equalsIgnoreCase(input.toString())) {
                        return i;
                    }
                }
            }
        }
    }

    static int promptForEditQuantity() {
        System.out.print("\nSelect new quantity: ");
        Scanner scan = new Scanner(System.in);

        return scanInt(scan, 0, 999);
    }

    static void promptContinue() {
        Scanner scan = new Scanner(System.in);
        System.out.print("Press 'Enter' to continue...");

        try {
            scan.nextLine();
        } catch (Exception e) { }
    }

    static boolean promptEndProcess() {
        return promptEndProcess("\nDo you want to continue?");
    }
    static boolean promptEndProcess(String prompt){
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
