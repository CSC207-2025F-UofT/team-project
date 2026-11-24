package interface_adapter;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

/** Runs blocking use cases on a worker and delivers unexpected failures on the UI executor. */
public final class UiTasks {
  private final Executor worker;
  private final Executor ui;

  /** Accepts Swing or deterministic test schedulers. */
  public UiTasks(Executor worker, Executor ui) {
    this.worker = worker;
    this.ui = ui;
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
