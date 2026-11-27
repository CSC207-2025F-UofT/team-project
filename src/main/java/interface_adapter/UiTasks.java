package interface_adapter;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.function.Consumer;

/** Runs blocking use cases on a worker and delivers unexpected failures on the UI executor. */
public final class UiTasks {
  private final Executor worker;
  private final Executor ui;

  /** Accepts Swing or deterministic test schedulers. */
  public UiTasks(Executor worker, Executor ui) {
    this.worker = worker;
    this.ui = ui;
  }

  /** Reads on the worker and delivers the result or failure on the UI thread. */
  public <T> void read(Callable<T> operation, Consumer<T> onSuccess, Runnable onFailure) {
    run(
        () -> {
          T result;
          try {
            result = operation.call();
          } catch (Exception exception) {
            ui.execute(onFailure);
            return;
          }
          ui.execute(() -> onSuccess.accept(result));
        },
        onFailure);
  }

  /** Schedules one operation; the presenter handles its normal and expected failure outcomes. */
  public void run(Runnable operation, Runnable onUnexpectedFailure) {
    try {
      worker.execute(
          () -> {
            try {
              operation.run();
            } catch (RuntimeException exception) {
              ui.execute(onUnexpectedFailure);
            }
          });
    } catch (RejectedExecutionException exception) {
      ui.execute(onUnexpectedFailure);
    }
  }
}
