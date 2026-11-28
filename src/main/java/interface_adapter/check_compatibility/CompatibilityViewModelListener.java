package interface_adapter.check_compatibility;

/**
 * Told when the compatibility state changed.
 */
public interface CompatibilityViewModelListener {

    /**
     * The compatibility state was updated.
     */
    void stateChanged();
}
