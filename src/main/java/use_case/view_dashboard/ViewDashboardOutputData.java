package use_case.view_dashboard;

import java.util.List;

/** Everything the dashboard shows. Each list is sorted newest first. */
public class ViewDashboardOutputData {

    private final List<QuestRow> inProgress;
    private final List<QuestRow> completed;
    private final List<QuestRow> cancelled;
    private final int totalExpEarned;

    public ViewDashboardOutputData(List<QuestRow> inProgress, List<QuestRow> completed,
                                   List<QuestRow> cancelled, int totalExpEarned) {
        this.inProgress = List.copyOf(inProgress);
        this.completed = List.copyOf(completed);
        this.cancelled = List.copyOf(cancelled);
        this.totalExpEarned = totalExpEarned;
    }

    public List<QuestRow> getInProgress() {
        return inProgress;
    }

    public List<QuestRow> getCompleted() {
        return completed;
    }

    public List<QuestRow> getCancelled() {
        return cancelled;
    }

    public int getTotalCompleted() {
        return completed.size();
    }

    public int getTotalExpEarned() {
        return totalExpEarned;
    }

    /** True when the user has no quests at all, so the view shows the empty state. */
    public boolean isEmpty() {
        return inProgress.isEmpty() && completed.isEmpty() && cancelled.isEmpty();
    }
}
