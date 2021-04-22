package xml_system.model;

public class Item {
    private int id;
    private String name;
    private int price;

    public Item() {
        this.id = 0;
        this.name = "";
        this.price = 1;
    }

    public Item(int id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Item ID: " + id + "\nItem name: " + name + "\nPrice: " + price + "\n\n";
    }
}
