package use_case.rest;

/**
 * Input boundary for resting at the residence (UC4).
 */
public interface RestInputBoundary {
    void execute();

    /** Tries again to save the checkpoint after a failed save (UC4 extension 7a). */
    void retrySave();
}
