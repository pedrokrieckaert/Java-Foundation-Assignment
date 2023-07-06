package src.presentation;

import src.data.pojo.CartItem;
import src.data.pojo.Product;
import src.service.ProductService;

import java.util.Scanner;

import static src.validation.Validation.invalidIndex;
import static src.validation.Validation.isNullOrBlank;

public abstract class Interaction {
    static <T> T promptForProduct(){
        Scanner scan = new Scanner(System.in);

        System.out.print("Please enter the name or id of a product: ");

        while(true) {
            if (scan.hasNextInt()) {
                int index = scan.nextInt();

                //Validate if the input is within src.data.repository bounds
                if (!invalidIndex(index)) {
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
    /**
     * CLI prompt to retrieve a product from the src.data.repository by id or Name
     * @return Product
     */
    static Product oldPromptForProduct(ProductService productService) {
        Scanner scan = new Scanner(System.in);
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

    static CartItem promptForQuantity(Product product) {
        Scanner scan = new Scanner(System.in);
        System.out.print("\nPlease select the quantity: ");
        int quantity = 0;

        while (true) {
            if (scan.hasNextInt()) {
                quantity = scan.nextInt();

                if (invalidIndex(quantity)) {
                    System.out.println("Please input a valid number (not negative): ");
                    continue;
                }
            } else {
                System.out.println("Please input a number: ");
                scan.next();
                continue;
            }

            return new CartItem(product, quantity);
        }

    }

    static boolean promptEndProcess() {
        Scanner scan = new Scanner(System.in);
        System.out.print("\nDo you want to continue? (y/n): ");

        while (true) {
            String input = scan.nextLine();
            if (isNullOrBlank(input)) {
                scan.skip("");
                continue;
            }

            if (!input.equalsIgnoreCase("y") && !input.equalsIgnoreCase("n")) {
                System.out.println("Please provide a valid input (y/n): ");
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
