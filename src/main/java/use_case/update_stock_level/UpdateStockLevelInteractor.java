package use_case.update_stock_level;

import entity.IngredientName;
import entity.Pantry;

/**
 * Changes one pantry item's stock level and saves the pantry.
 */
public final class UpdateStockLevelInteractor implements UpdateStockLevelInputBoundary {
    private static final String ITEM_NOT_FOUND = "Ingredient is not in the pantry.";
    private static final String INVALID_INPUT = "Ingredient name and stock level are required.";

    private final UpdateStockLevelDataAccessInterface dataAccess;
    private final UpdateStockLevelOutputBoundary presenter;

    /**
     * Creates the interactor.
     *
     * @param dataAccess the pantry data access
     * @param presenter the output boundary
     */
    public UpdateStockLevelInteractor(UpdateStockLevelDataAccessInterface dataAccess,
                                      UpdateStockLevelOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(UpdateStockLevelInputData inputData) {
        if (inputData == null || inputData.getStockLevel() == null) {
            prepareFailure(INVALID_INPUT);
            return;
        }

        final IngredientName name;
        try {
            name = IngredientName.fromRawName(inputData.getIngredientName());
        }
        catch (IllegalArgumentException exception) {
            prepareFailure(INVALID_INPUT);
            return;
        }

        final Pantry pantry = dataAccess.loadPantry();
        try {
            pantry.setStockLevel(name, inputData.getStockLevel());
        }
        catch (IllegalArgumentException exception) {
            prepareFailure(ITEM_NOT_FOUND);
            return;
        }

        dataAccess.savePantry(pantry);
        final String displayName = pantry.find(name).orElseThrow()
                .getName().getDisplayName();
        presenter.prepareSuccessView(new UpdateStockLevelOutputData(
                displayName, inputData.getStockLevel(), "", false));
    }

    private void prepareFailure(String message) {
        presenter.prepareFailView(new UpdateStockLevelOutputData(
                null, null, message, true));
    }
}
