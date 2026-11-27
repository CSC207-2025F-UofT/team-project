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
    submit(new SearchJobsInputData(query), "Searching configured boards...");
  }

  /** Submits keywords and an optional location for cross-company search. */
  public void execute(String query, String location) {
    execute(query, "", location);
  }

  /** Searches job keywords with independent company and location filters. */
  public void execute(String query, String company, String location) {
    submit(SearchJobsInputData.withFilters(query, company, location), "Searching job listings...");
  }

  /** Continues the last submitted search, preserving the displayed rows. */
  public void loadMore() {
    if (viewModel.getState().hasMore()) {
      submit(SearchJobsInputData.more(), "Loading more internships...");
    }
  }

  /** Sends the company selected at the time the user pressed Search. */
  public void executeForBoard(String query, String board) {
    submit(new SearchJobsInputData(query, "", board), "Searching company boards...");
  }

  private void submit(SearchJobsInputData input, String message) {
    if (viewModel.getState().isLoading()) {
      return;
    }
    SearchJobsState previous = viewModel.getState();
    List<SearchJobsState.Row> rows = input.isLoadMore() ? previous.getRows() : List.of();
    viewModel.setState(new SearchJobsState(rows, message, "", true, false));
    try {
      worker.execute(
          () -> {
            try {
              useCase.execute(input);
            } catch (RuntimeException exception) {
              // Unexpected adapter failures must not leave the screen permanently disabled.
              showFailure(
                  "Search could not be completed. Please try again.", rows, input.isLoadMore());
            }
          });
    } catch (RejectedExecutionException exception) {
      showFailure("Search is unavailable. Please reopen the Finder.", rows, input.isLoadMore());
    }
  }

  private void showFailure(String message, List<SearchJobsState.Row> rows, boolean retry) {
    uiExecutor.execute(
        () -> viewModel.setState(new SearchJobsState(rows, message, "", false, true, retry)));
  }
}
