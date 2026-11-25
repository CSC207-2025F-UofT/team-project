package use_case.cancel_quest;

import entity.UserQuest;

import java.time.Clock;
import java.time.LocalDateTime;

/**
 * Cancels an in-progress quest. No EXP is given. Cancelling still works if
 * the quest was deleted from the catalogue, so the user isn't stuck with it.
 */
public class CancelQuestInteractor implements CancelQuestInputBoundary {

    private final CancelQuestUserQuestDataAccessInterface userQuestData;
    private final CancelQuestOutputBoundary presenter;
    private final Clock clock;

    public CancelQuestInteractor(CancelQuestUserQuestDataAccessInterface userQuestData,
                                 CancelQuestOutputBoundary presenter,
                                 Clock clock) {
        this.userQuestData = userQuestData;
        this.presenter = presenter;
        this.clock = clock;
    }

    @Override
    public void execute(CancelQuestInputData inputData) {
        UserQuest userQuest = userQuestData.getUserQuest(
                inputData.getUsername(), inputData.getQuestId());
        if (userQuest == null) {
            presenter.prepareFailView("You haven't accepted this quest.");
            return;
        }
        if (!userQuest.isInProgress()) {
            presenter.prepareFailView("This quest is already "
                    + userQuest.getStatus().name().toLowerCase() + ".");
            return;
        }

        userQuest.cancel(LocalDateTime.now(clock));
        userQuestData.saveUserQuest(userQuest);

        presenter.prepareSuccessView(new CancelQuestOutputData(inputData.getQuestId()));
    }
}
