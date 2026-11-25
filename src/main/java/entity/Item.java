package entity;

public class Item {

    public enum Type {
        FOOD,
        MEDICINE
    }

    private final String id;
    private final String name;
    private final Type type;
    private final int price;
    private final int effectAmount;

    public Item(String id, String name, Type type,
                int price, int effectAmount) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Item ID is required.");
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Item name is required.");
        }

        if (type == null) {
            throw new IllegalArgumentException("Item type is required.");
        }

        if (price <= 0 || effectAmount <= 0) {
            throw new IllegalArgumentException(
                    "Price and effect amount must be positive."
            );
        }

        this.id = id;
        this.name = name;
        this.type = type;
        this.price = price;
        this.effectAmount = effectAmount;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    public int getEffectAmount() {
        return effectAmount;
    }
}