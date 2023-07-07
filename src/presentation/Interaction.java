package src.presentation;

import src.data.pojo.CartItem;
import src.data.pojo.Product;

import java.util.Scanner;

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

                if (invalidIndex(quantity, 0, 99999)) {
                    System.out.print("\nPlease input a positive number: ");
                    continue;
                }
            } else {
                System.out.print("\nPlease input a valid positive number: ");
                scan.next();
                continue;
            }

            return new CartItem(product, quantity);
        }

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
