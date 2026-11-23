package interface_adapter.view_pet_details;

/**
 * Something that wants to hear when the pet detail page changes.
 */
public interface PetDetailViewModelListener {

    /**
     * Called after the state of the detail page changed.
     */
    void stateChanged();
}
