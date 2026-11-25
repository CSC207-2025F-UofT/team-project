package use_case.cancel_quest;

/** Which quest was cancelled, so the dashboard can move it to the Cancelled list. */
public class CancelQuestOutputData {

    private final String questId;

    public CancelQuestOutputData(String questId) {
        this.questId = questId;
    }

    public String getQuestId() {
        return questId;
    }
}
