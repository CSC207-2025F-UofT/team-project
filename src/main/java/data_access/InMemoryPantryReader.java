package data_access;

import entity.Pantry;
import use_case.generate_shopping_list.PantryReader;

/**
 * In-memory read-only pantry store used until the file DAO is ready.
 */
public class InMemoryPantryReader implements PantryReader {

    private Pantry pantry = new Pantry();

    @Override
    public Pantry getPantry() {
        return pantry;
    }

    /**
     * Seeds the pantry for demos and tests.
     *
     * @param pantry the pantry to store
     */
    public void seed(Pantry pantry) {
        this.pantry = pantry;
    }
}
