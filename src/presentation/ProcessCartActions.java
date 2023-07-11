package src.presentation;

enum ProcessCartActions implements ActionEnum{
    EDIT (1, "Edit item"),
    REMOVE (2, "Remove item"),
    DONE (3, "Done");

    private final int i;
    private final String s;

    ProcessCartActions(int i, String s) {
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
