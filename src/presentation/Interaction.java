package src.presentation;

import src.data.pojo.CartItem;
import src.data.pojo.Product;

import java.util.List;
import java.util.Scanner;

import static src.presentation.UserInput.scanIntOrString;
import static src.validation.Validation.invalidIndex;
import static src.validation.Validation.isNullOrBlank;

public abstract class Interaction {
    static ProcessActions promptForAction() {
        Scanner scan = new Scanner(System.in);

        System.out.println("\nWhat would you like to do?");

        for (ProcessActions action : ProcessActions.values()) {
            System.out.println("[" + action.i + "]" + "\t" + action.s);
        }

        System.out.print("\nSelect index or name: ");

        while(true) {
            if (scan.hasNextInt()) {
                int index = scan.nextInt();

                if (!invalidIndex(index, 1, ProcessActions.values().length)) {
                    for (ProcessActions action : ProcessActions.values()) {
                        if (action.i == index) {
                            return action;
                        }
                    }
                } else {
                    System.out.print("\nInvalid id, please select a valid id (0 - 11): ");
                    scan.nextLine(); //Next line trap
                }
            } else {
                String input = scan.nextLine();

                if (isNullOrBlank(input)) {
                    scan.skip("");
                    continue;
                }

                for (ProcessActions action : ProcessActions.values()) {
                    if (action.s.equalsIgnoreCase(input)) {
                        return action;
                    }
                }
            }
        }
    }

    static ProcessCartActions promptCartAction() {
        Scanner scan = new Scanner(System.in);

        for (ProcessCartActions action : ProcessCartActions.values()) {
            System.out.println(action.toString());
        }

        while (true) {
            Object input = scanIntOrString(scan, 1, ProcessCartActions.values().length);

            if (input instanceof Integer) {
                int index = Integer.parseInt(String.valueOf(input));

                for (ProcessCartActions action : ProcessCartActions.values()) {
                    if (action.i == index) {
                        return action;
                    }
                }
            } else if (input instanceof String) {
                String name = input.toString();

                for (ProcessCartActions action : ProcessCartActions.values()) {
                    if (action.s.equalsIgnoreCase(name)) {
                        return action;
                    }
                }

            }
        }
    }

    private static String actionStringSwitch(String input){

        return null;
    }

    private static int actionIntSwitch(int input) {

        return 0;
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
        return 0;
    }

    static int promptForEditQuantity() {

        return 1;
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
