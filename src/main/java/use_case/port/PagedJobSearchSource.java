package use_case.port;

import use_case.search_jobs.SearchJobsInputData;

/** A keyword search provider that exposes successive pages in its own ranking order. */
@FunctionalInterface
public interface PagedJobSearchSource extends JobSearchSource {
  /** Fetches one numbered page for the same keywords and location, starting at one. */
  Page fetchPage(SearchJobsInputData input, int pageNumber) throws JobSourceException;

  /** Fetches a page in the default keyword mode. */
  default Page fetchPage(String keywords, String location, int pageNumber)
      throws JobSourceException {
    return fetchPage(new SearchJobsInputData(keywords, location), pageNumber);
  }

  @Override
  default JobFetchResult search(SearchJobsInputData input) throws JobSourceException {
    return fetchPage(input, 1).getResult();
  }

  /** Provides the first raw page to callers using the single-retrieval port. */
  @Override
  default JobFetchResult search(String keywords, String location) throws JobSourceException {
    return fetchPage(keywords, location, 1).getResult();
  }

  /** A raw result page and the provider's total before Finder applies its title rule. */
  final class Page {
    private final JobFetchResult result;
    private final long totalCount;

    /** A negative total means the provider did not report a count. */
    public Page(JobFetchResult result, long totalCount) {
      this.result = result;
      this.totalCount = totalCount;
    }

    public JobFetchResult getResult() {
      return result;
    }

    public long getTotalCount() {
      return totalCount;
    }
  }
}
