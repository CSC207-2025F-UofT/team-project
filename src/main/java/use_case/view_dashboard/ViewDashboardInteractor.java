package use_case.view_dashboard;

import entity.QuestStatus;
import entity.SideQuest;
import entity.UserQuest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Builds the dashboard: the user's quests split into In Progress, Completed,
 * and Cancelled (newest first), plus total EXP earned from completed quests.
 */
public class ViewDashboardInteractor implements ViewDashboardInputBoundary {

    private final ViewDashboardUserQuestDataAccessInterface userQuestData;
    private final ViewDashboardSideQuestDataAccessInterface sideQuestData;
    private final ViewDashboardOutputBoundary presenter;

    public ViewDashboardInteractor(ViewDashboardUserQuestDataAccessInterface userQuestData,
                                   ViewDashboardSideQuestDataAccessInterface sideQuestData,
                                   ViewDashboardOutputBoundary presenter) {
        this.userQuestData = userQuestData;
        this.sideQuestData = sideQuestData;
        this.presenter = presenter;
    }

    @Override
    public void execute(ViewDashboardInputData inputData) {
        String username = inputData.getUsername();
        if (username == null || username.isBlank()) {
            presenter.prepareFailView("Please log in to see your dashboard.");
            return;
        }

        List<QuestRow> inProgress = new ArrayList<>();
        List<QuestRow> completed = new ArrayList<>();
        List<QuestRow> cancelled = new ArrayList<>();
        int totalExpEarned = 0;

        for (UserQuest userQuest : userQuestData.getUserQuests(username)) {
            SideQuest sideQuest = sideQuestData.getSideQuest(userQuest.getQuestId());
            if (sideQuest == null) {
                continue;   // quest was deleted from the catalogue: skip the row
            }
            QuestRow row = new QuestRow(userQuest.getQuestId(), sideQuest.getName(),
                    sideQuest.getExp(), userQuest.getLastUpdated());

            if (userQuest.getStatus() == QuestStatus.COMPLETED) {
                completed.add(row);
                totalExpEarned += sideQuest.getExp();
            } else if (userQuest.getStatus() == QuestStatus.CANCELLED) {
                cancelled.add(row);
            } else {
                inProgress.add(row);
            }
        }

        // Java note: Comparator.comparing(QuestRow::getDate) is like
        // key=lambda r: r.date in Python. .reversed() makes it newest first.
        Comparator<QuestRow> newestFirst = Comparator.comparing(QuestRow::getDate).reversed();
        inProgress.sort(newestFirst);
        completed.sort(newestFirst);
        cancelled.sort(newestFirst);

        presenter.prepareSuccessView(
                new ViewDashboardOutputData(inProgress, completed, cancelled, totalExpEarned));
    }
}
