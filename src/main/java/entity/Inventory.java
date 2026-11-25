package entity;

import java.util.HashMap;
import java.util.Map;

public class Inventory {

    private final String ownerUsername;
    private final Map<String, Integer> quantities = new HashMap<>();

    public Inventory(String ownerUsername) {
        if (ownerUsername == null || ownerUsername.isBlank()) {
            throw new IllegalArgumentException("Owner is required.");
        }

        this.ownerUsername = ownerUsername;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public int getQuantity(String itemId) {
        validateItemId(itemId);
        return quantities.getOrDefault(itemId, 0);
    }

    public void addItem(String itemId) {
        int quantity = getQuantity(itemId);
        quantities.put(itemId, Math.addExact(quantity, 1));
    }

    public void removeItem(String itemId) {
        int quantity = getQuantity(itemId);

        if (quantity == 0) {
            throw new IllegalStateException("You do not own this item.");
        }

        if (quantity == 1) {
            quantities.remove(itemId);
        } else {
            quantities.put(itemId, quantity - 1);
        }
    }

    private void validateItemId(String itemId) {
        if (itemId == null || itemId.isBlank()) {
            throw new IllegalArgumentException("Item ID is required.");
        }
    }
}