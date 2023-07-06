package src.presentation;

abstract class StringPrintFormats {
    static final int PAD_SMALL = 15;
    static final int PAD_LARGE = 30;
    static final int PAD_XL = 45;

    static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }
}
