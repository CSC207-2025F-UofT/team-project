package use_case.add_card;

public interface AddCardOutputBoundary {
    void prepareSuccessView(AddCardOutputData outputData);

    void prepareFailView(String error);
}