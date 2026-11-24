package use_case.add_pantry_item;

import java.time.Clock;
import java.time.LocalDate;
import java.util.Objects;

import entity.IngredientName;
import entity.Pantry;
import entity.PantryItem;
import entity.StockLevel;

/**
 * Adds one item to the pantry with an initial stock level of enough.
 */
public final class AddPantryItemInteractor implements AddPantryItemInputBoundary {
    private static final String INVALID_INPUT = "Ingredient name is required.";
    private static final String INVALID_EXPIRY_DATE = "Expiry date cannot be in the past.";
    private static final String DUPLICATE_ITEM = "Ingredient is already in the pantry.";

    private final AddPantryItemDataAccessInterface dataAccess;
    private final AddPantryItemOutputBoundary presenter;
    private final Clock clock;

    /**
     * Creates the interactor.
     *
     * @param dataAccess the pantry data access
     * @param presenter the output boundary
     * @param clock the clock used to determine the current date
     */
    public AddPantryItemInteractor(AddPantryItemDataAccessInterface dataAccess,
                                    AddPantryItemOutputBoundary presenter,
                                    Clock clock) {
        this.dataAccess = Objects.requireNonNull(dataAccess, "data access cannot be null");
        this.presenter = Objects.requireNonNull(presenter, "presenter cannot be null");
        this.clock = Objects.requireNonNull(clock, "clock cannot be null");
    }

    @Override
    public void execute(AddPantryItemInputData inputData) {
        if (inputData == null) {
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

        final LocalDate expiryDate = inputData.getExpiryDate();
        if (expiryDate != null && expiryDate.isBefore(LocalDate.now(clock))) {
            prepareFailure(INVALID_EXPIRY_DATE);
            return;
        }

        final Pantry pantry = Objects.requireNonNull(
                dataAccess.loadPantry(), "data access returned a null pantry");
        try {
            pantry.add(new PantryItem(name, expiryDate, StockLevel.ENOUGH));
        }
        catch (IllegalArgumentException exception) {
            prepareFailure(DUPLICATE_ITEM);
            return;
        }

        dataAccess.savePantry(pantry);
        presenter.prepareSuccessView(new AddPantryItemOutputData(
                name.getDisplayName(), expiryDate, StockLevel.ENOUGH, "", false));
    }

    private void prepareFailure(String message) {
        presenter.prepareFailView(new AddPantryItemOutputData(
                null, null, null, message, true));
    }
}
