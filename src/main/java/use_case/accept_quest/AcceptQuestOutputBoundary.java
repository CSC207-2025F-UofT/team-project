package use_case.accept_quest;

public interface AcceptQuestOutputBoundary {

    void prepareSuccessView(AcceptQuestOutputData outputData);

    void prepareFailView(String errorMessage);
}
