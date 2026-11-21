package stockprototype.comparison.ui;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import stockprototype.comparison.CompareStocks;
import stockprototype.comparison.ComparisonResult;

final class ComparisonController {
    private final CompareStocks compareStocks;
    private final ComparisonPresenter presenter;
    private boolean running;

    ComparisonController(CompareStocks compareStocks, ComparisonPresenter presenter) {
        this.compareStocks = Objects.requireNonNull(compareStocks);
        this.presenter = presenter;
    }

    void compare(String first, String second, int sessions) {
        if (running) {
            return;
        }
        running = true;
        presenter.start();
        new SwingWorker<ComparisonResult, Void>() {
            @Override
            protected ComparisonResult doInBackground() throws IOException {
                return compareStocks.compare(first, second, sessions);
            }

            @Override
            protected void done() {
                running = false;
                try {
                    presenter.present(get());
                } catch (InterruptedException exception) {
                    Thread.currentThread().interrupt();
                    presenter.fail("The comparison was interrupted. Please try again.");
                } catch (ExecutionException exception) {
                    Throwable cause = exception.getCause();
                    String message = "Unable to complete the comparison. Please try again.";
                    if ((cause instanceof IOException || cause instanceof IllegalArgumentException)
                            && cause.getMessage() != null) {
                        message = cause.getMessage();
                    }
                    presenter.fail(message);
                }
            }
        }.execute();
    }
}
