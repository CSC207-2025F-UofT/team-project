package use_case.complete_quest;

/**
 * What the interactor calls when it's done. The presenter implements this.
 * The interactor never knows about Swing or the view.
 */
public interface CompleteQuestOutputBoundary {

    void prepareSuccessView(CompleteQuestOutputData outputData);

    void prepareFailView(String errorMessage);

    /** Asks the UI to offer a journal entry for this quest (Pauline's use case). */
    void promptJournal(String questId);
}
