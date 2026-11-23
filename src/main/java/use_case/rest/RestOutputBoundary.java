package use_case.rest;

/**
 * Output boundary for resting at the residence (UC4).
 */
public interface RestOutputBoundary {
    void prepareSuccessView(RestOutputData outputData);

    /** @param saved whether the retried save succeeded */
    void prepareSaveRetryView(boolean saved);
}
