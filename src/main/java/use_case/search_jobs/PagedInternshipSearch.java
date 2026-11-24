package use_case.search_jobs;

import entity.JobPosting;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import use_case.common.InternshipClassifier;
import use_case.port.JobFetchResult;
import use_case.port.JobSourceException;
import use_case.port.PagedJobSearchSource;
import use_case.port.SourceFailure;

/** Remembers the fetched pages and undisplayed matches for one Finder search. */
final class PagedInternshipSearch {
  private static final int BATCH_SIZE = 10;
  private final PagedJobSearchSource source;
  private final SearchJobsInputData criteria;
  private final String companyFilter;
  private final List<JobPosting> matches = new ArrayList<>();
  private final Set<String> seenIds = new HashSet<>();
  private final List<SourceFailure> failures = new ArrayList<>();
  private int nextPage = 1;
  private int displayed;
  private long rawCount;
  private boolean exhausted;
  private String stopReason = "";

  PagedInternshipSearch(PagedJobSearchSource source, SearchJobsInputData criteria) {
    this.source = source;
    this.criteria = criteria;
    this.companyFilter = criteria.getCompany().toLowerCase(Locale.ROOT);
  }

  SearchJobsOutputData nextBatch() throws JobSourceException {
    final int target = displayed + BATCH_SIZE;
    try {
      while (!exhausted && matches.size() < target) {
        consume(source.fetchPage(criteria, nextPage));
        nextPage++;
      }
    } catch (JobSourceException exception) {
      if (nextPage == 1) {
        throw exception;
      }
      return result(target, exception.getMessage());
    }
    return result(target, stopReason);
  }

  private void consume(PagedJobSearchSource.Page page) {
    JobFetchResult result = page.getResult();
    final int previousCount = seenIds.size();
    for (JobPosting job : result.getJobs()) {
      if (seenIds.add(job.getStableId())
          && InternshipClassifier.isInternship(job.getTitle())
          && matchesCompany(job)) {
        matches.add(job);
      }
    }
    failures.addAll(result.getFailures());
    rawCount += result.getJobs().size();
    exhausted =
        result.getJobs().isEmpty()
            || (page.getTotalCount() >= 0 && rawCount >= page.getTotalCount());
    if (!exhausted && page.getTotalCount() < 0 && previousCount == seenIds.size()) {
      exhausted = true;
      stopReason = "The provider repeated earlier listings.";
    }
  }

  private boolean matchesCompany(JobPosting job) {
    return companyFilter.isEmpty()
        || job.getCompany().toLowerCase(Locale.ROOT).contains(companyFilter);
  }

  private SearchJobsOutputData result(int target, String warning) {
    displayed = Math.min(target, matches.size());
    return new SearchJobsOutputData(
        matches.subList(0, displayed), failures, warning, displayed < matches.size() || !exhausted);
  }
}
