package use_case.load_pantry;

import java.util.List;

/**
 * Output data for the pantry page.
 */
public final class LoadPantryOutputData {
    private final List<LoadPantryItemOutputData> items;

    /**
     * Creates pantry output data.
     *
     * @param items pantry items ordered by expiry date
     */
    public LoadPantryOutputData(List<LoadPantryItemOutputData> items) {
        this.items = List.copyOf(items);
    }

    /**
     * Returns pantry items ordered by expiry date.
     *
     * @return an immutable list of items
     */
    public List<LoadPantryItemOutputData> getItems() {
        return items;
    }

    /**
     * Returns whether the pantry contains no items.
     *
     * @return true when the pantry is empty
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }
}
