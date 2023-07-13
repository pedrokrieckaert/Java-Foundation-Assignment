package src.utilities;

public abstract class Validation {
    /**
     * Check if a String object is null or value is blank.
     * @param input String
     * @return boolean
     */
    public static boolean isNullOrBlank(String input) {
        return input == null || input.isBlank();
    }

    /**
     * Check if an int is within bounds of a maximum and minimum value
     * @param index int
     * @return boolean
     */
    public static boolean invalidIndex(int index, int min, int max) {
        return index < min || index > max;
    }
}
