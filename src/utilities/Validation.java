package src.utilities;

public abstract class Validation {
    /**
     * Check if scanned line has input
     * @param input String
     * @return boolean
     */
    public static boolean isNullOrBlank(String input) {
        return input == null || input.isBlank();
    }

    /**
     * Check if scanned int is within src.data.repository bounds
     * @param index int
     * @return boolean
     */
    public static boolean invalidIndex(int index, int min, int max) {
        return index < min || index > max;
    }
}
