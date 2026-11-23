package use_case.edit_card;

public interface EditCardOutputBoundary {
    void prepareSuccessView(EditCardOutputData outputData);

    void prepareFailView(String error);
}
