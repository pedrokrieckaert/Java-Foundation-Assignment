package src.presentation;

enum ProcessCartActions {
    EDIT (1, "Edit item"),
    REMOVE (2, "Remove item"),
    DONE (3, "Done");

    final int i;
    final String s;

    ProcessCartActions(int i, String s) {
        this.i = i;
        this.s = s;
    }

    @Override
    public String toString() {
        return "[" + i + "]\t" + s;
    }
}
