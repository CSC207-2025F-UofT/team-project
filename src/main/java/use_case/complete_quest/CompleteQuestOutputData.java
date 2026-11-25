package use_case.complete_quest;

/**
 * What the interactor hands to the presenter after a successful completion.
 * Plain values only, no entities, so the UI never touches business objects.
 */
public class CompleteQuestOutputData {

    private final String questId;
    private final String questName;
    private final int expGained;
    private final int newTotalExp;
    private final int newLevel;
    private final boolean leveledUp;

    public CompleteQuestOutputData(String questId, String questName, int expGained,
                                   int newTotalExp, int newLevel, boolean leveledUp) {
        this.questId = questId;
        this.questName = questName;
        this.expGained = expGained;
        this.newTotalExp = newTotalExp;
        this.newLevel = newLevel;
        this.leveledUp = leveledUp;
    }

    public String getQuestId() {
        return questId;
    }

    public String getQuestName() {
        return questName;
    }

    public int getExpGained() {
        return expGained;
    }

    public int getNewTotalExp() {
        return newTotalExp;
    }

    public int getNewLevel() {
        return newLevel;
    }

    public boolean isLeveledUp() {
        return leveledUp;
    }
}
