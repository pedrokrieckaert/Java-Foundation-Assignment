package src.utilities;

public abstract class StringPrintFormats {
    public static final int PAD_SMALL = 15;
    public static final int PAD_LARGE = 30;
    public static final int PAD_XL = 45;

    /**
     * Formats a String to have <i>n</i> characters.
     * Iterating through the String and adding spaces to the remaining desired character amount to the <b>right</b>.
     * @param s String string to format
     * @param n int desired string length
     * @return String formatted
     */
    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    /**
     * Formats a String to have <i>n</i> characters.
     * Iterating through the String and adding spaces to the remaining desired character amount to the <b>left</b>.
     * @param s String string to format
     * @param n int desired string length
     * @return String formatted
     */
    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }
}
