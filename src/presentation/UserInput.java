package src.presentation;

import java.util.Scanner;

import static src.validation.Validation.invalidIndex;
import static src.validation.Validation.isNullOrBlank;

abstract class UserInput {

    static <T> T scanIntOrString(Scanner scan, int INT_MIN, int INT_MAX) {
        System.out.print("\nSelect index or name: ");

        if (scan.hasNextInt()) {
            int index = scan.nextInt();

            //Validate if the input is within src.data.repository bounds
            if (!invalidIndex(index, INT_MIN, INT_MAX)) {
                return (T) Integer.valueOf(index);
            } else {
                System.out.print("\nInvalid id, please select a valid id (" + INT_MIN + " - " + INT_MAX + "): ");
                scan.nextLine(); //Next line trap
            }
        } else {
            String input = scan.nextLine();

            //Validate if the input is null or blank
            if (isNullOrBlank(input)) {
                scan.skip("");
            }

            return (T) input;
        }
        return null;
    }
}
