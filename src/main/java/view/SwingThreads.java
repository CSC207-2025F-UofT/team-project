package view;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.util.concurrent.ExecutionException;

final class SwingThreads {
    private static final System.Logger LOGGER = System.getLogger(SwingThreads.class.getName());

    private SwingThreads() { }

    /** Presenters update view models from background threads, but Swing may only be touched on the EDT. */
    static void onEdt(Runnable task) {
        if (SwingUtilities.isEventDispatchThread()) {
            task.run();
        } else {
            SwingUtilities.invokeLater(task);
        }
    }

    /**
     * Runs a controller call off the EDT so network requests don't freeze the window. View model updates made by
     * the call reach the EDT before {@code onFinished} runs. {@code onFailure} runs first if the call threw.
     */
    static void inBackground(Runnable call, Runnable onFailure, Runnable onFinished) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                call.run();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                    onFailure.run();
                } catch (ExecutionException failure) {
                    LOGGER.log(System.Logger.Level.ERROR, "Background action failed.", failure.getCause());
                    onFailure.run();
                }
                onFinished.run();
            }
        }.execute();
    }
}
