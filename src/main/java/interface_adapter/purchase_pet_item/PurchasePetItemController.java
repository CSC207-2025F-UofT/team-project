package interface_adapter.purchase_pet_item;

import use_case.purchase_pet_item.PurchasePetItemInputBoundary;
import use_case.purchase_pet_item.PurchasePetItemInputData;

public class PurchasePetItemController {
    private final PurchasePetItemInputBoundary purchaseInteractor;

    public PurchasePetItemController(
            PurchasePetItemInputBoundary purchaseInteractor) {
        this.purchaseInteractor = purchaseInteractor;
    }

    public void execute(String username, String itemId) {
        PurchasePetItemInputData inputData =
                new PurchasePetItemInputData(username, itemId);

        purchaseInteractor.execute(inputData);
    }
}