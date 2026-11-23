package use_case.view_history;

/**
 * How the View History use case reports its result.
 * The presenter implements this.
 */
public interface ViewHistoryOutputBoundary {

    /** Called when there is at least one saved message. */
    void prepareSuccessView(ViewHistoryOutputData outputData);

    /** Called when there is nothing to show. */
    void prepareFailView(String errorMessage);
}