package src.utilities;

public abstract class StringPrintFormats {
    public static final int PAD_SMALL = 15;
    public static final int PAD_LARGE = 30;
    public static final int PAD_XL = 45;

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }
}
