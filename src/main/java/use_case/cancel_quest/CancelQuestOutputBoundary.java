package use_case.cancel_quest;

public interface CancelQuestOutputBoundary {

    void prepareSuccessView(CancelQuestOutputData outputData);

    void prepareFailView(String errorMessage);
}
