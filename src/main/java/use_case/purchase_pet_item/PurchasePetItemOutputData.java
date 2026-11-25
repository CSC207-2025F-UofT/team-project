package use_case.purchase_pet_item;

public class PurchasePetItemOutputData {
    private final String itemId;
    private final String itemName;
    private final int remainingCoins;
    private final int quantityOwned;

    public PurchasePetItemOutputData(
            String itemId,
            String itemName,
            int remainingCoins,
            int quantityOwned) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.remainingCoins = remainingCoins;
        this.quantityOwned = quantityOwned;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getRemainingCoins() {
        return remainingCoins;
    }

    public int getQuantityOwned() {
        return quantityOwned;
    }
}