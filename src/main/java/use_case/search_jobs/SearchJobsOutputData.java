package use_case.search_jobs;

import entity.JobPosting;
import java.util.List;
import use_case.port.SourceFailure;

/** Matching snapshots and incomplete-source warnings, including for zero matches. */
public final class SearchJobsOutputData {
  private final List<JobPosting> jobs;
  private final List<SourceFailure> failures;

  /** Copies matching snapshots and failures so consumers cannot modify shared source results. */
  public SearchJobsOutputData(List<JobPosting> jobs, List<SourceFailure> failures) {
    this.jobs = List.copyOf(jobs);
    this.failures = List.copyOf(failures);
  }

  public List<JobPosting> getJobs() {
    return jobs;
  }

  public List<SourceFailure> getFailures() {
    return failures;
  }
}
