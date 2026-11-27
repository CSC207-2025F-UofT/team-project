package interface_adapter.search_jobs;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import use_case.search_jobs.SearchJobsOutputBoundary;
import use_case.search_jobs.SearchJobsOutputData;

/** Converts use-case outcomes to display rows and publishes them through the UI executor. */
public final class SearchJobsPresenter implements SearchJobsOutputBoundary {
  private final SearchJobsViewModel viewModel;
  private final Executor uiExecutor;

  /** Uses the supplied UI executor to publish state safely from a background search. */
  public SearchJobsPresenter(SearchJobsViewModel viewModel, Executor uiExecutor) {
    this.viewModel = viewModel;
    this.uiExecutor = uiExecutor;
  }

  @Override
  public void prepareSuccessView(SearchJobsOutputData output) {
    List<SearchJobsState.Row> rows =
        output.getJobs().stream()
            .map(
                job ->
                    new SearchJobsState.Row(
                        job.getStableId(),
                        job.getTitle(),
                        job.getCompany(),
                        job.getLocation().isBlank() ? "Not specified" : job.getLocation(),
                        job.getApplicationUrl()))
            .collect(Collectors.toList());
    String message =
        rows.isEmpty()
            ? "0 matches. No matching internships were found in the retrieved postings."
            : rows.size() + (rows.size() == 1 ? " matching internship." : " matching internships.");
    String unavailable =
        output.getFailures().stream()
            .map(failure -> failure.getSourceId() + " (" + failure.getReason().name() + ")")
            .collect(Collectors.joining(", "));
    String detail =
        (unavailable.isEmpty() ? "" : "Unavailable: " + unavailable + " ")
            + output.getIncompleteReason();
    String warning = detail.isBlank() ? "" : "Results are incomplete. " + detail.strip();
    uiExecutor.execute(
        () ->
            viewModel.setState(
                new SearchJobsState(rows, message, warning, false, false, output.hasMore())));
  }

  @Override
  public void prepareFailView(String message) {
    uiExecutor.execute(
        () -> viewModel.setState(new SearchJobsState(List.of(), message, "", false, true)));
  }
}
