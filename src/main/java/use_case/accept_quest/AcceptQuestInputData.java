package use_case.accept_quest;

/** Who is accepting which quest. */
public class AcceptQuestInputData {

    private final String username;
    private final String questId;

    public AcceptQuestInputData(String username, String questId) {
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
