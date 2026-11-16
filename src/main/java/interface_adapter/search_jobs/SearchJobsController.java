package interface_adapter.search_jobs;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import use_case.search_jobs.SearchJobsInputBoundary;
import use_case.search_jobs.SearchJobsInputData;

/** Builds input data and schedules use-case work off the UI thread. */
public final class SearchJobsController {
  private final SearchJobsInputBoundary useCase;
  private final SearchJobsViewModel viewModel;
  private final Executor worker;
  private final Executor uiExecutor;

  /** Injects the use case and separate worker/UI schedulers for production or tests. */
  public SearchJobsController(
      SearchJobsInputBoundary useCase,
      SearchJobsViewModel viewModel,
      Executor worker,
      Executor uiExecutor) {
    this.useCase = useCase;
    this.viewModel = viewModel;
    this.worker = worker;
    this.uiExecutor = uiExecutor;
  }

  /** Submits at most one search at a time; callers invoke this on the UI thread. */
  public void execute(String query) {
    if (viewModel.getState().isLoading()) {
      return;
    }
    viewModel.setState(
        new SearchJobsState(List.of(), "Searching configured boards...", "", true, false));
    try {
      worker.execute(
          () -> {
            try {
              useCase.execute(new SearchJobsInputData(query));
            } catch (RuntimeException exception) {
              // Unexpected adapter failures must not leave the screen permanently disabled.
              showFailure("Search could not be completed. Please try again.");
            }
          });
    } catch (RejectedExecutionException exception) {
      showFailure("Search is unavailable. Please reopen the Finder.");
    }
  }

  private void showFailure(String message) {
    uiExecutor.execute(
        () -> viewModel.setState(new SearchJobsState(List.of(), message, "", false, true)));
  }
}
