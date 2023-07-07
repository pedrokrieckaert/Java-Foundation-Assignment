package src.presentation;

enum ProcessActions {
    ADD(1, "Add item"),
    PRODUCTS(2, "See products"),
    HOURS(3, "See opening times"),
    CART(4, "See your basket"),
    TERMINATE(5, "Checkout");

    final int i;
    final String s;
    ProcessActions(int i, String s) {
        this.i = i;
        this.s = s;
    }
}
