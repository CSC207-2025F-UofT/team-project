package use_case.port;

import use_case.search_jobs.SearchJobsInputData;

/** Retrieves postings matching keywords and an optional location before internship filtering. */
@FunctionalInterface
public interface JobSearchSource {
  /** Returns keyword matches in source order, or a display-safe error if retrieval fails. */
  JobFetchResult search(String keywords, String location) throws JobSourceException;

  /** Accepts the complete request for providers that support a specific company board. */
  default JobFetchResult search(SearchJobsInputData input) throws JobSourceException {
    return search(input.getQuery(), input.getLocation());
  }
}
