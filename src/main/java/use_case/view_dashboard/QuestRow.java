package use_case.view_dashboard;

import java.time.LocalDateTime;

/**
 * One row on the dashboard. Plain values only, so the view never touches
 * the SideQuest or UserQuest entities.
 */
public class QuestRow {

    private final String questId;
    private final String name;
    private final int exp;
    private final LocalDateTime date;

    public QuestRow(String questId, String name, int exp, LocalDateTime date) {
        this.questId = questId;
        this.name = name;
        this.exp = exp;
        this.date = date;
    }

    public String getQuestId() {
        return questId;
    }

    public String getName() {
        return name;
    }

    public int getExp() {
        return exp;
    }

    /** Start date for in-progress quests, otherwise the completed/cancelled date. */
    public LocalDateTime getDate() {
        return date;
    }
}
