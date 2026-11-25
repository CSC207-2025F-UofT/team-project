package use_case.cancel_quest;

/** Who is cancelling which quest. */
public class CancelQuestInputData {

    private final String username;
    private final String questId;

    public CancelQuestInputData(String username, String questId) {
        this.username = username;
        this.questId = questId;
    }

    public String getUsername() {
        return username;
    }

    public String getQuestId() {
        return questId;
    }
}
