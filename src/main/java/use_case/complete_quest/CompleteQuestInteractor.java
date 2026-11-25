package use_case.complete_quest;

import entity.SideQuest;
import entity.User;
import entity.UserQuest;

import java.time.Clock;
import java.time.LocalDateTime;

/**
 * Completes an in-progress quest: checks the anti-farming rules, marks it
 * completed, awards EXP, and asks the UI to offer a journal entry.
 *
 * Why the daily cap lives here and the 10-minute rule lives in UserQuest:
 * the 10-minute rule is about one quest, so it belongs to that entity. The
 * daily cap counts across all of a user's quests, which needs a storage
 * query, so it can only live in the use case layer.
 */
public class CompleteQuestInteractor implements CompleteQuestInputBoundary {

    /** Max quests a user can complete in one calendar day. */
    public static final int MAX_COMPLETIONS_PER_DAY = 5;

    private final CompleteQuestUserQuestDataAccessInterface userQuestData;
    private final CompleteQuestSideQuestDataAccessInterface sideQuestData;
    private final CompleteQuestUserDataAccessInterface userData;
    private final CompleteQuestOutputBoundary presenter;
    private final Clock clock;

    /**
     * Java note: this is "dependency injection". The interactor receives the
     * things it depends on instead of creating them, so tests can swap in
     * fakes. In the real app, pass Clock.systemDefaultZone() for the clock.
     */
    public CompleteQuestInteractor(CompleteQuestUserQuestDataAccessInterface userQuestData,
                                   CompleteQuestSideQuestDataAccessInterface sideQuestData,
                                   CompleteQuestUserDataAccessInterface userData,
                                   CompleteQuestOutputBoundary presenter,
                                   Clock clock) {
        this.userQuestData = userQuestData;
        this.sideQuestData = sideQuestData;
        this.userData = userData;
        this.presenter = presenter;
        this.clock = clock;
    }

    @Override
    public void execute(CompleteQuestInputData inputData) {
        String username = inputData.getUsername();
        String questId = inputData.getQuestId();
        LocalDateTime now = LocalDateTime.now(clock);

        User user = userData.getUser(username);
        if (user == null) {
            presenter.prepareFailView("User not found.");
            return;
        }

        UserQuest userQuest = userQuestData.getUserQuest(username, questId);
        if (userQuest == null) {
            presenter.prepareFailView("You haven't accepted this quest.");
            return;
        }
        if (!userQuest.isInProgress()) {
            presenter.prepareFailView("This quest is already "
                    + userQuest.getStatus().name().toLowerCase() + ".");
            return;
        }

        SideQuest sideQuest = sideQuestData.getSideQuest(questId);
        if (sideQuest == null) {
            presenter.prepareFailView("This quest no longer exists.");
            return;
        }

        if (userQuestData.countCompletedOn(username, now.toLocalDate()) >= MAX_COMPLETIONS_PER_DAY) {
            presenter.prepareFailView("You've hit today's limit of "
                    + MAX_COMPLETIONS_PER_DAY + " quests. Come back tomorrow!");
            return;
        }

        if (!userQuest.meetsMinimumTime(now)) {
            long wait = userQuest.minutesUntilCompletable(now);
            presenter.prepareFailView("Too soon! Wait " + wait + " more minute"
                    + (wait == 1 ? "" : "s") + " before completing.");
            return;
        }

        int levelBefore = user.getLevel();
        int expGained = sideQuest.getExp();

        userQuest.complete(now);
        // User.addExp rejects 0, but a side quest is allowed to be worth 0 EXP.
        if (expGained > 0) {
            user.addExp(expGained);
        }

        userQuestData.saveUserQuest(userQuest);
        userData.saveUser(user);

        CompleteQuestOutputData outputData = new CompleteQuestOutputData(
                questId,
                sideQuest.getName(),
                expGained,
                user.getExp(),
                user.getLevel(),
                user.getLevel() > levelBefore);

        presenter.prepareSuccessView(outputData);
        presenter.promptJournal(questId);
    }
}
