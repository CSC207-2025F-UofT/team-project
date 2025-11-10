package entity;

public class MenuItem {
    private String name;
    private float price;
    private String description;
    private String type;
    // private final int rating; (NOT SURE IF WE HAVE RATINGS FOR ITEMS)

    public MenuItem(String name, float price, String description, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.description = description;
    }

    public String getName() {
        return this.name;
    }

    public float getPrice() {
        return this.price;
    }

    public String getDescription() {
        return this.description;
    }

    public String getType() {
        return this.type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setType(String type) {
        this.type = type;
    }
}
