package use_case.search_jobs;

/** Receives use-case outcomes without depending on a particular user interface. */
public interface SearchJobsOutputBoundary {
  /** Presents retrieved matches, including valid empty results and partial-source warnings. */
  void prepareSuccessView(SearchJobsOutputData output);

  /** Presents validation or retrieval failure; this must not appear as a successful zero count. */
  void prepareFailView(String message);
}
