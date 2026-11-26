package use_case.open_pet_shop;

import entity.Inventory;

public class OpenPetShopInteractor
        implements OpenPetShopInputBoundary {

    private final OpenPetShopDataAccessInterface itemDataAccess;
    private final OpenPetShopCoinDataAccessInterface coinDataAccess;
    private final OpenPetShopOutputBoundary presenter;

    public OpenPetShopInteractor(
            OpenPetShopDataAccessInterface itemDataAccess,
            OpenPetShopCoinDataAccessInterface coinDataAccess,
            OpenPetShopOutputBoundary presenter) {
        this.itemDataAccess = itemDataAccess;
        this.coinDataAccess = coinDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(OpenPetShopInputData inputData) {
        String username = inputData.getUsername();

        if (username == null || username.isBlank()) {
            presenter.prepareFailView("Please log in first.");
            return;
        }

        int coinBalance;

        try {
            coinBalance = coinDataAccess.getCoinBalance(username);
        }
        catch (IllegalArgumentException exception) {
            presenter.prepareFailView(exception.getMessage());
            return;
        }

        Inventory inventory =
                itemDataAccess.getOrCreateInventory(username);

        OpenPetShopOutputData outputData =
                new OpenPetShopOutputData(
                        username,
                        coinBalance,
                        inventory.getQuantity("basic_food"),
                        inventory.getQuantity("basic_medicine"));

        presenter.prepareSuccessView(outputData);
    }
}