package src.presentation;

import java.util.Scanner;

import static src.utilities.Validation.invalidIndex;
import static src.utilities.Validation.isNullOrBlank;

abstract class UserInput {
    /**
     * Scans the CLI for an int or String returning a generic of the input.
     * @param scan Scanner
     * @param INT_MIN Lower bound const for int value validation
     * @param INT_MAX Upper bound const for int value validation
     * @return T of the scanned input
     */
    static <T> T scanIntOrString(Scanner scan, int INT_MIN, int INT_MAX) {
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

    /**
     * Scans the CLI for an int and returns an int.
     * @param scan Scanner
     * @param INT_MIN Lower bound const for int value validation
     * @param INT_MAX Upper bound const for int value validation
     * @return int Scanned input
     */
    static int scanInt (Scanner scan, int INT_MIN, int INT_MAX) {
        int input = Integer.MIN_VALUE;

        while (input < INT_MIN || input > INT_MAX) {
            if (scan.hasNextInt()) {
                input = scan.nextInt();
                if (invalidIndex(input, INT_MIN, INT_MAX)) {
                    System.out.print("\nOut of range.\nPlease input a number within range (" + INT_MIN + " - " + INT_MAX + "): ");
                }
            } else {
                System.out.print("\nNot a Number.\nPlease input a number within range (" + INT_MIN + " - " + INT_MAX + "): ");
                scan.next();
            }
        }

        return input;
    }
}
