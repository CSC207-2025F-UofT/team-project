package use_case.accept_quest;

import entity.QuestStatus;
import entity.SideQuest;
import entity.UserQuest;

import java.time.Clock;
import java.time.LocalDateTime;

/**
 * Starts a quest for a user: records it as IN_PROGRESS from now.
 *
 * Rules:
 * - Can't accept a quest that doesn't exist.
 * - Can't accept a quest that's already in progress.
 * - Can't redo a completed quest (stops EXP farming and keeps history intact).
 * - A cancelled quest CAN be accepted again; the new attempt replaces it.
 */
public class AcceptQuestInteractor implements AcceptQuestInputBoundary {

    private final AcceptQuestUserQuestDataAccessInterface userQuestData;
    private final AcceptQuestSideQuestDataAccessInterface sideQuestData;
    private final AcceptQuestOutputBoundary presenter;
    private final Clock clock;

    public AcceptQuestInteractor(AcceptQuestUserQuestDataAccessInterface userQuestData,
                                 AcceptQuestSideQuestDataAccessInterface sideQuestData,
                                 AcceptQuestOutputBoundary presenter,
                                 Clock clock) {
        this.userQuestData = userQuestData;
        this.sideQuestData = sideQuestData;
        this.presenter = presenter;
        this.clock = clock;
    }

    @Override
    public void execute(AcceptQuestInputData inputData) {
        String username = inputData.getUsername();
        String questId = inputData.getQuestId();

        SideQuest sideQuest = sideQuestData.getSideQuest(questId);
        if (sideQuest == null) {
            presenter.prepareFailView("This quest no longer exists.");
            return;
        }

        UserQuest existing = userQuestData.getUserQuest(username, questId);
        if (existing != null && existing.getStatus() == QuestStatus.IN_PROGRESS) {
            presenter.prepareFailView("You're already doing this quest.");
            return;
        }
        if (existing != null && existing.getStatus() == QuestStatus.COMPLETED) {
            presenter.prepareFailView("You've already completed this quest.");
            return;
        }

        UserQuest userQuest = new UserQuest(username, questId, LocalDateTime.now(clock));
        userQuestData.saveUserQuest(userQuest);

        presenter.prepareSuccessView(new AcceptQuestOutputData(questId, sideQuest.getName()));
    }
}
