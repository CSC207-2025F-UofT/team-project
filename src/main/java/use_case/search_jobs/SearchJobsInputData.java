package use_case.search_jobs;

/** Raw query input; validation belongs to the interactor. */
public final class SearchJobsInputData {
  private final String query;

  /** Preserves the raw query, including null or blank values for use-case validation. */
  public SearchJobsInputData(String query) {
    this.query = query;
  }

  public String getQuery() {
    return query;
  }
}
