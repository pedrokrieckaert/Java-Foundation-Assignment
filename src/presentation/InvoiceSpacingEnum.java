package src.presentation;

public enum InvoiceSpacingEnum {
    TWO("  "),
    THREE("   "),
    FOUR("    "),
    FIVE("     "),
    SIX("      "),
    SEVEN("       "),
    EIGHT("        "),
    NINE("         "),
    TEN("          ");

    public final String spaces;
    InvoiceSpacingEnum(String spaces) {
        this.spaces = spaces;
    }
}
