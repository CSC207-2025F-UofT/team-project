package use_case.purchase_pet_item;

import entity.Inventory;
import entity.Item;

public class PurchasePetItemInteractor
        implements PurchasePetItemInputBoundary {

    private final PurchasePetItemDataAccessInterface itemDataAccess;
    private final PurchasePetItemCoinDataAccessInterface coinDataAccess;
    private final PurchasePetItemOutputBoundary presenter;

    public PurchasePetItemInteractor(
            PurchasePetItemDataAccessInterface itemDataAccess,
            PurchasePetItemCoinDataAccessInterface coinDataAccess,
            PurchasePetItemOutputBoundary presenter) {
        this.itemDataAccess = itemDataAccess;
        this.coinDataAccess = coinDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(PurchasePetItemInputData inputData) {
        String username = inputData.getUsername();
        String itemId = inputData.getItemId();

        if (username == null || username.isBlank()) {
            presenter.prepareFailView("Please log in first.");
            return;
        }

        if (itemId == null || itemId.isBlank()) {
            presenter.prepareFailView("Please select an item.");
            return;
        }

        Item item = itemDataAccess.findItemById(itemId);

        if (item == null) {
            presenter.prepareFailView("This item is not available.");
            return;
        }

        Inventory inventory =
                itemDataAccess.getOrCreateInventory(username);

        if (inventory.getQuantity(itemId) == Integer.MAX_VALUE) {
            presenter.prepareFailView(
                    "You cannot carry any more of this item.");
            return;
        }

        int remainingCoins;

        try {
            remainingCoins = coinDataAccess.spendCoins(
                    username, item.getPrice());
        }
        catch (IllegalArgumentException | IllegalStateException exception) {
            presenter.prepareFailView(exception.getMessage());
            return;
        }

        inventory.addItem(itemId);
        itemDataAccess.saveInventory(inventory);

        PurchasePetItemOutputData outputData =
                new PurchasePetItemOutputData(
                        item.getId(),
                        item.getName(),
                        remainingCoins,
                        inventory.getQuantity(itemId));

        presenter.prepareSuccessView(outputData);
    }
}