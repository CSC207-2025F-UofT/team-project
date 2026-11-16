package use_case.search_jobs;

import entity.JobPosting;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import use_case.common.InternshipClassifier;
import use_case.port.JobFetchResult;
import use_case.port.JobSource;
import use_case.port.JobSourceException;

/** UC4: validate a manual query, retrieve internships, and match every keyword. */
public final class SearchJobsInteractor implements SearchJobsInputBoundary {
  private final JobSource jobs;
  private final SearchJobsOutputBoundary output;

  /** Injects retrieval and output boundaries without depending on their concrete adapters. */
  public SearchJobsInteractor(JobSource jobs, SearchJobsOutputBoundary output) {
    this.jobs = jobs;
    this.output = output;
  }

  @Override
  public void execute(SearchJobsInputData input) {
    String query = input.getQuery() == null ? "" : input.getQuery();
    List<String> terms =
        Arrays.stream(query.toLowerCase(Locale.ROOT).split("(?U)\\s+"))
            .filter(term -> !term.isEmpty())
            .distinct()
            .collect(Collectors.toList());
    if (terms.isEmpty()) {
      output.prepareFailView("Enter at least one keyword.");
      return;
    }
    final JobFetchResult fetched;
    try {
      fetched = jobs.fetch();
    } catch (JobSourceException exception) {
      output.prepareFailView("Search could not be completed. Please try again.");
      return;
    }
    List<JobPosting> matches =
        fetched.getJobs().stream()
            .filter(job -> InternshipClassifier.isInternship(job.getTitle()))
            .filter(job -> matchesAll(job, terms))
            .collect(Collectors.toList());
    output.prepareSuccessView(new SearchJobsOutputData(matches, fetched.getFailures()));
  }

  private boolean matchesAll(JobPosting job, List<String> terms) {
    String text = (job.getTitle() + " " + job.getDescriptionText()).toLowerCase(Locale.ROOT);
    return terms.stream().allMatch(text::contains);
  }
}
