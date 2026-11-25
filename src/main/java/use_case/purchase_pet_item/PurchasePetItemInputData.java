package use_case.purchase_pet_item;

public class PurchasePetItemInputData {
    private final String username;
    private final String itemId;

    public PurchasePetItemInputData(String username, String itemId) {
        this.username = username;
        this.itemId = itemId;
    }

    public String getUsername() {
        return username;
    }

    public String getItemId() {
        return itemId;
    }
}