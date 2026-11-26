package interface_adapter.open_pet_shop;

import interface_adapter.ViewManagerModel;
import interface_adapter.pet_shop.PetShopState;
import interface_adapter.pet_shop.PetShopViewModel;
import use_case.open_pet_shop.OpenPetShopOutputBoundary;
import use_case.open_pet_shop.OpenPetShopOutputData;

import java.util.function.Consumer;

public class OpenPetShopPresenter
        implements OpenPetShopOutputBoundary {

    private final PetShopViewModel petShopViewModel;
    private final ViewManagerModel viewManagerModel;
    private final Consumer<String> showError;

    public OpenPetShopPresenter(
            PetShopViewModel petShopViewModel,
            ViewManagerModel viewManagerModel,
            Consumer<String> showError) {
        this.petShopViewModel = petShopViewModel;
        this.viewManagerModel = viewManagerModel;
        this.showError = showError;
    }

    @Override
    public void prepareSuccessView(OpenPetShopOutputData outputData) {
        PetShopState state = new PetShopState();

        state.setUsername(outputData.getUsername());
        state.setCoinBalance(outputData.getCoinBalance());
        state.setFoodQuantity(outputData.getFoodQuantity());
        state.setMedicineQuantity(outputData.getMedicineQuantity());

        petShopViewModel.setState(state);
        petShopViewModel.notifyStateChanged();

        viewManagerModel.setState(PetShopViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        showError.accept(errorMessage);
    }
}