package xml_system.xml_access;

public enum WarehouseXMLComponents {
    WAREHOUSE("cns:warehouse"),
    SECTIONS("sections"),
    SECTION("section"),
    SECTION_ID("id"),
    SECTION_NAME("name"),
    ITEMS("items"),
    ITEM("item"),
    ITEM_ID("id"),
    ITEM_NAME("name"),
    PRICE("price");

    private final String name;

    WarehouseXMLComponents(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
