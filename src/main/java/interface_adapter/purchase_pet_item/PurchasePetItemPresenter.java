package interface_adapter.purchase_pet_item;

import interface_adapter.pet_shop.PetShopState;
import interface_adapter.pet_shop.PetShopViewModel;
import use_case.purchase_pet_item.PurchasePetItemOutputBoundary;
import use_case.purchase_pet_item.PurchasePetItemOutputData;

public class PurchasePetItemPresenter
        implements PurchasePetItemOutputBoundary {

    private final PetShopViewModel petShopViewModel;

    public PurchasePetItemPresenter(PetShopViewModel petShopViewModel) {
        this.petShopViewModel = petShopViewModel;
    }

    @Override
    public void prepareSuccessView(PurchasePetItemOutputData outputData) {
        PetShopState state = petShopViewModel.getState();

        state.setCoinBalance(outputData.getRemainingCoins());

        if ("basic_food".equals(outputData.getItemId())) {
            state.setFoodQuantity(outputData.getQuantityOwned());
        }
        else if ("basic_medicine".equals(outputData.getItemId())) {
            state.setMedicineQuantity(outputData.getQuantityOwned());
        }

        state.setMessage("Purchased " + outputData.getItemName() + "!");

        petShopViewModel.setState(state);
        petShopViewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        PetShopState state = petShopViewModel.getState();

        state.setMessage(errorMessage);

        petShopViewModel.setState(state);
        petShopViewModel.notifyStateChanged();
    }
}