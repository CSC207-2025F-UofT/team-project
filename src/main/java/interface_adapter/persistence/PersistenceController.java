package interface_adapter.persistence;

import use_case.ManageSavesInputBoundary;
import use_case.load_game.LoadGameInputBoundary;
import use_case.save_game.SaveGameInputBoundary;

import javax.swing.SwingWorker;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Runs persistence away from the EDT while the game action buttons are disabled. */
public final class PersistenceController {
    private static final Logger LOGGER = Logger.getLogger(PersistenceController.class.getName());

    private final SaveGameInputBoundary saveGame;
    private final LoadGameInputBoundary loadGame;
    private final ManageSavesInputBoundary manageSaves;
    private final Supplier<String> username;
    private final PersistenceViewModel feedback;

    public PersistenceController(SaveGameInputBoundary saveGame, LoadGameInputBoundary loadGame,
                                 ManageSavesInputBoundary manageSaves, Supplier<String> username,
                                 PersistenceViewModel feedback) {
        this.saveGame = saveGame;
        this.loadGame = loadGame;
        this.manageSaves = manageSaves;
        this.username = username;
        this.feedback = feedback;
    }

    public void save() {
        execute("Saving game...", saveGame::execute);
    }

    public void load() {
        execute("Finding saved games...", manageSaves::list);
    }

    public void save(String name) {
        execute("Saving game...", account -> saveGame.execute(account, name));
    }

    public void load(UUID runId) {
        execute("Loading game...", account -> loadGame.execute(account, runId));
    }

    public void delete(UUID runId) {
        execute("Deleting saved game...", account -> manageSaves.delete(account, runId));
    }

    private void execute(String message, Consumer<String> operation) {
        if (feedback.getState().busy()) {
            return;
        }
        String account = username.get();
        feedback.setState(new PersistenceViewModel.State(true, message));
        feedback.firePropertyChanged();
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                operation.accept(account);
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    showUnexpectedFailure(e);
                } catch (ExecutionException e) {
                    showUnexpectedFailure(e);
                }
            }
        }.execute();
    }

    private void showUnexpectedFailure(Exception e) {
        LOGGER.log(Level.WARNING, "Persistence operation failed", e);
        feedback.setState(new PersistenceViewModel.State(false, "Something went wrong. Please try again."));
        feedback.firePropertyChanged();
    }
}
