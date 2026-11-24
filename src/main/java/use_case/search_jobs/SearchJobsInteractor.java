package use_case.search_jobs;

import entity.JobPosting;
import java.util.List;
import java.util.stream.Collectors;
import use_case.common.InternshipClassifier;
import use_case.port.JobFetchResult;
import use_case.port.JobSearchSource;
import use_case.port.JobSource;
import use_case.port.JobSourceException;
import use_case.port.PagedJobSearchSource;

/** UC4: validate a manual search and keep internships from the chosen search source. */
public final class SearchJobsInteractor implements SearchJobsInputBoundary {
  private PagedInternshipSearch activeSearch;
  private final JobSearchSource jobs;
  private final SearchJobsOutputBoundary output;

  /** Injects retrieval and output boundaries without depending on their concrete adapters. */
  public SearchJobsInteractor(JobSource jobs, SearchJobsOutputBoundary output) {
    this(new BoardKeywordSearch(jobs), output);
  }

  /** Uses a provider that can already search by keyword and location. */
  public SearchJobsInteractor(JobSearchSource jobs, SearchJobsOutputBoundary output) {
    this.jobs = jobs;
    this.output = output;
  }

  @Override
  public void execute(SearchJobsInputData input) {
    if (input.isLoadMore()) {
      if (activeSearch == null) {
        output.prepareFailView("Start a search before loading more internships.");
      } else {
        publishNextBatch();
      }
      return;
    }
    activeSearch = null;
    String query = normalize(input.getQuery());
    if (query.isEmpty()) {
      output.prepareFailView("Enter at least one keyword.");
      return;
    }
    String location = normalize(input.getLocation());
    if (jobs instanceof PagedJobSearchSource) {
      SearchJobsInputData criteria =
          SearchJobsInputData.withFilters(query, normalize(input.getCompany()), location);
      activeSearch = new PagedInternshipSearch((PagedJobSearchSource) jobs, criteria);
      publishNextBatch();
      return;
    }
    try {
      JobFetchResult fetched =
          jobs.search(new SearchJobsInputData(query, location, input.getBoard()));
      List<JobPosting> matches =
          fetched.getJobs().stream()
              .filter(job -> InternshipClassifier.isInternship(job.getTitle()))
              .collect(Collectors.toList());
      output.prepareSuccessView(new SearchJobsOutputData(matches, fetched.getFailures()));
    } catch (JobSourceException exception) {
      output.prepareFailView(exception.getMessage());
    }
  }

  private void publishNextBatch() {
    try {
      output.prepareSuccessView(activeSearch.nextBatch());
    } catch (JobSourceException exception) {
      output.prepareFailView(exception.getMessage());
    }
  }

  private static String normalize(String text) {
    return text == null ? "" : text.replaceAll("(?U)\\s+", " ").trim();
  }
}
