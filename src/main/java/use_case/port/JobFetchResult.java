package use_case.port;

import entity.JobPosting;
import java.util.List;
import java.util.stream.Collectors;

/** Immutable result; partial failure and a successful empty result remain distinguishable. */
public final class JobFetchResult {
  private final List<JobPosting> jobs;
  private final List<String> successfulSources;
  private final List<SourceFailure> failures;

  /**
   * Creates a successful or partially successful result with defensive collection copies.
   *
   * @param jobs snapshots from successful boards
   * @param successfulSources IDs of successful boards, including boards with no jobs
   * @param failures details of unavailable boards
   */
  public JobFetchResult(
      List<JobPosting> jobs, List<String> successfulSources, List<SourceFailure> failures) {
    if (successfulSources.isEmpty()) {
      throw new IllegalArgumentException("Use JobSourceException when no source succeeded.");
    }
    this.jobs = List.copyOf(jobs);
    this.successfulSources = List.copyOf(successfulSources);
    this.failures = List.copyOf(failures);
  }

  public List<JobPosting> getJobs() {
    return jobs;
  }

  public List<String> getSuccessfulSources() {
    return successfulSources;
  }

  public int getSuccessCount() {
    return successfulSources.size();
  }

  public List<SourceFailure> getFailures() {
    return failures;
  }

  /** Returns the unavailable board IDs used by recommendation and search warnings. */
  public List<String> getFailedBoards() {
    return failures.stream()
        .map(SourceFailure::getSourceId)
        .collect(Collectors.toUnmodifiableList());
  }
}
