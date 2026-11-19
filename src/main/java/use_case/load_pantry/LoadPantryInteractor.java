package use_case.load_pantry;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import entity.Pantry;
import entity.PantryItem;

/**
 * Loads the pantry and prepares its items for display.
 */
public final class LoadPantryInteractor implements LoadPantryInputBoundary {
    private static final int EXPIRING_SOON_DAYS = 3;

    private final LoadPantryDataAccessInterface dataAccess;
    private final LoadPantryOutputBoundary presenter;
    private final Clock clock;

    /**
     * Creates the interactor.
     *
     * @param dataAccess the pantry data access
     * @param presenter the output boundary
     * @param clock the clock used to determine the current date
     */
    public LoadPantryInteractor(LoadPantryDataAccessInterface dataAccess,
                               LoadPantryOutputBoundary presenter,
                               Clock clock) {
        this.dataAccess = Objects.requireNonNull(dataAccess, "data access cannot be null");
        this.presenter = Objects.requireNonNull(presenter, "presenter cannot be null");
        this.clock = Objects.requireNonNull(clock, "clock cannot be null");
    }

    @Override
    public void execute(LoadPantryInputData inputData) {
        Objects.requireNonNull(inputData, "input data cannot be null");

        final Pantry pantry = Objects.requireNonNull(
                dataAccess.loadPantry(), "data access returned a null pantry");
        final LocalDate today = LocalDate.now(clock);
        final List<LoadPantryItemOutputData> items = pantry.sortedByExpiry().stream()
                .map(item -> toOutputData(item, today))
                .toList();

        presenter.prepareSuccessView(new LoadPantryOutputData(items));
    }

    private LoadPantryItemOutputData toOutputData(PantryItem item, LocalDate today) {
        return new LoadPantryItemOutputData(
                item.getName().getDisplayName(),
                item.getName().getKey(),
                item.getExpiryDate(),
                item.getStockLevel(),
                item.isExpired(today),
                item.expiresWithin(today, EXPIRING_SOON_DAYS)
        );
    }
}
