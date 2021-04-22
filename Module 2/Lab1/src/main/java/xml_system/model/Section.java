package xml_system.model;

import java.util.List;

public class Section {
    private int id;
    private String name;
    private List<Item> items;

    public Section() {
        this.id = 0;
        this.name = "";
        this.items = null;
    }

    public Section(int id, String name, List<Item> items) {
        this.id = id;
        this.name = name;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Item> getItems() {
        return items;
    }

    public Item getItem(int id) {
        return items.stream()
                .filter(item -> item.getId() == id)
                .findAny()
                .orElse(null);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public boolean addItem(Item item) {
        if (getItem(item.getId()) != null) {
            return false;
        }
        items.add(item);
        return true;
    }

    public boolean deleteItem(int id) {
        Item item = getItem(id);
        if (item == null) {
            return false;
        }
        items.remove(item);
        return true;
    }

    public boolean updateItem(int id, String newName, int newPrice) {
        Item item = getItem(id);
        if (item == null) {
            return false;
        }
        item.setName(newName);
        item.setPrice(newPrice);
        return true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("Section ID: ")
                .append(id)
                .append("\nSection name: ")
                .append(name)
                .append("\nItems:\n\n");
        for (Item item : items) {
            result.append("   ");
            result.append(item.toString().replace("\n", "\n   "));
        }

        return result.toString();
    }
}
