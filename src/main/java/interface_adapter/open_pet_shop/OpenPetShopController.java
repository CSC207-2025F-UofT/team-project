package interface_adapter.open_pet_shop;

import use_case.open_pet_shop.OpenPetShopInputBoundary;
import use_case.open_pet_shop.OpenPetShopInputData;

public class OpenPetShopController {
    private final OpenPetShopInputBoundary interactor;

    public OpenPetShopController(OpenPetShopInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(String username) {
        OpenPetShopInputData inputData =
                new OpenPetShopInputData(username);

        interactor.execute(inputData);
    }
}