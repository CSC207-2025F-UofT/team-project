package use_case;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Lists saves and removes a selected snapshot after confirmation by the view. */
public final class ManageSavesInteractor implements ManageSavesInputBoundary {
    public interface OutputBoundary {
        void prepareSavedGamesView(List<SavedGameSummary> saves, String message);

        void prepareFailView(String message);
    }

    private static final Logger LOGGER = Logger.getLogger(ManageSavesInteractor.class.getName());
    private final SaveGameDataAccessInterface saves;
    private final OutputBoundary presenter;

    public ManageSavesInteractor(SaveGameDataAccessInterface saves, OutputBoundary presenter) {
        this.saves = saves;
        this.presenter = presenter;
    }

    @Override
    public void list(String username) {
        execute(username, null);
    }

    @Override
    public void delete(String username, UUID runId) {
        if (runId == null) {
            presenter.prepareFailView("Select a saved game first.");
            return;
        }
        execute(username, runId);
    }

    private void execute(String username, UUID runId) {
        if (username == null || username.isBlank()) {
            presenter.prepareFailView("Sign in to manage your saved games.");
            return;
        }
        try {
            String message = "";
            if (runId != null) {
                message = saves.delete(username, runId) ? "Saved game deleted." : "That saved game is no longer available.";
            }
            presenter.prepareSavedGamesView(saves.list(username), message);
        } catch (GamePersistenceException e) {
            LOGGER.log(Level.WARNING, "Could not manage saved games", e);
            presenter.prepareFailView(runId == null ? "Couldn't list your saved games. Please try again."
                    : "Couldn't refresh your saved games. Reopen Saved Games to check the result.");
        }
    }
}
