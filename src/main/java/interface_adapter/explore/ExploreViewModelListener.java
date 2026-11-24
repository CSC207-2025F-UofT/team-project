package interface_adapter.explore;

/**
 * Implemented by the Explore screen so it hears about changes to the
 * ExploreViewModel (same pattern as the team's LoginViewModelListener).
 */
public interface ExploreViewModelListener {

    /**
     * The explore state was updated, so the screen should redraw.
     */
    void stateChanged();

    /**
     * The inventory is full (Use Case 2, extension 7a), so the screen should ask
     * the player which item to drop.
     */
    void inventoryFull();
}
