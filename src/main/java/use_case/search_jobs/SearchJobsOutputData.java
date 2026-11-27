package use_case.search_jobs;

import entity.JobPosting;
import java.util.List;
import use_case.port.SourceFailure;

/** Matching snapshots and incomplete-source warnings, including for zero matches. */
public final class SearchJobsOutputData {
  private final List<JobPosting> jobs;
  private final List<SourceFailure> failures;
  private final String incompleteReason;
  private final boolean hasMore;

  /** Copies matching snapshots and failures so consumers cannot modify shared source results. */
  public SearchJobsOutputData(List<JobPosting> jobs, List<SourceFailure> failures) {
    this(jobs, failures, "", false);
  }

  /** Preserves partial results for a source without a continuation. */
  public SearchJobsOutputData(
      List<JobPosting> jobs, List<SourceFailure> failures, String incompleteReason) {
    this(jobs, failures, incompleteReason, false);
  }

  /** Preserves matches and the display-safe reason a paginated search stopped early. */
  public SearchJobsOutputData(
      List<JobPosting> jobs,
      List<SourceFailure> failures,
      String incompleteReason,
      boolean hasMore) {
    this.hasMore = hasMore;
    this.jobs = List.copyOf(jobs);
    this.failures = List.copyOf(failures);
    this.incompleteReason = incompleteReason;
  }

  /** Returns whether another batch can be requested. */
  public boolean hasMore() {
    return hasMore;
  }

  public String getIncompleteReason() {
    return incompleteReason;
  }

  public List<JobPosting> getJobs() {
    return jobs;
  }

  public List<SourceFailure> getFailures() {
    return failures;
  }
}
