package use_case.search_jobs;

/** The manual internship-search operation exposed to controllers. */
public interface SearchJobsInputBoundary {
  /** Executes a search and reports exactly one outcome through the output boundary. */
  void execute(SearchJobsInputData input);
}
