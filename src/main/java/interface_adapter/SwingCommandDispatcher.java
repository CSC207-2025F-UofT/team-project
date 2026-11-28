package interface_adapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import javax.swing.SwingUtilities;

/** One admission covers worker execution and all EDT updates queued by that command. */
public final class SwingCommandDispatcher implements CommandDispatcher, AutoCloseable {
    private final ExecutorService worker = Executors.newSingleThreadExecutor();
    private final AtomicBoolean admitted = new AtomicBoolean();
    private final GameViewModel model;
    private final Consumer<RuntimeException> onFailure;

    public SwingCommandDispatcher(GameViewModel model, Consumer<RuntimeException> onFailure) {
        this.model = model;
        this.onFailure = onFailure;
    }

    public void dispatch(Runnable command) {
        if (!admitted.compareAndSet(false, true)) return;
        Runnable schedule = () -> {
            try {
                model.setBusy(true);
                worker.execute(() -> {
                    try {
                        command.run();
                    } catch (RuntimeException failure) {
                        onFailure.accept(failure);
                    } finally {
                        SwingUtilities.invokeLater(this::release);
                    }
                });
            } catch (RuntimeException failure) {
                try {
                    onFailure.accept(failure);
                } finally {
                    SwingUtilities.invokeLater(this::release);
                }
            }
        };
        if (SwingUtilities.isEventDispatchThread()) schedule.run();
        else SwingUtilities.invokeLater(schedule);
    }

    private void release() {
        try {
            model.setBusy(false);
        } finally {
            admitted.set(false);
        }
    }

    public void close() { worker.shutdown(); }
}
