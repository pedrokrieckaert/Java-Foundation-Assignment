package src.presentation;

abstract class StringPrintFormats {
    static final int padSmall = 15;
    static final int padLarge = 30;
    static final int padXL = 45;

    static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }
}
