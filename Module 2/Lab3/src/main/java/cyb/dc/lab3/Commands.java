package cyb.dc.lab3;

public enum Commands {
    CREATE_SECTION("Create section"),
    CREATE_ITEM("Create item"),
    DELETE_SECTION("Delete section"),
    DELETE_ITEM("Delete item"),
    UPDATE_SECTION("Update section"),
    UPDATE_ITEM("Update item"),
    FIND_SECTION("Find section"),
    FIND_ITEM("Find item"),
    FIND_ITEMS_BY_SECTION("Find items by section"),
    SHOW_ALL_SECTIONS("Show all sections"),
    SHOW_ALL_ITEMS("Show all items");

    private final String name;

    Commands(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
