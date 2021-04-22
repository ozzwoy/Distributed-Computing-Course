package xml_system.ui;

public enum Operations {
    CREATE_SECTION("Create new section"),
    CREATE_ITEM("Create new item"),
    DELETE_SECTION("Delete section"),
    DELETE_ITEM("Delete item"),
    UPDATE_SECTION("Update section"),
    UPDATE_ITEM("Update item"),
    FIND_SECTION("Find section"),
    FIND_ITEM("Find item"),
    SHOW_ALL_SECTIONS("Show all sections");

    private final String name;

    Operations(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
