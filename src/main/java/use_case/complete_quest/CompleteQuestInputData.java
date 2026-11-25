package use_case.complete_quest;

/** What the controller hands to the interactor: who is completing which quest. */
public class CompleteQuestInputData {

    private final String username;
    private final String questId;

    public CompleteQuestInputData(String username, String questId) {
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
