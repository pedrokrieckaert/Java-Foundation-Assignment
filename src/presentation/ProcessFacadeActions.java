package src.presentation;

enum ProcessFacadeActions implements ActionEnum{
    ADD(1, "Add item"),
    PRODUCTS(2, "See products"),
    HOURS(3, "See opening times"),
    CART(4, "See your basket"),
    FINALIZE(5, "Checkout"),
    TERMINATE(6, "Exit");

    private final int i;
    private final String s;

    ProcessFacadeActions(int i, String s) {
        this.i = i;
        this.s = s;
    }

    @Override
    public String toString() {
        return "[" + i + "]\t" + s;
    }

    @Override
    public String getString() {
        return s;
    }

    @Override
    public int getInt() {
        return i;
    }
}
