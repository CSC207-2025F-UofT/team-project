package use_case.accept_quest;

/** What the presenter needs to say "Quest accepted!". */
public class AcceptQuestOutputData {

    private final String questId;
    private final String questName;

    public AcceptQuestOutputData(String questId, String questName) {
        this.questId = questId;
        this.questName = questName;
    }

    public String getQuestId() {
        return questId;
    }

    public String getQuestName() {
        return questName;
    }
}
