package use_case.search_jobs;

import entity.JobPosting;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import use_case.port.CompanyBoardSource;
import use_case.port.JobFetchResult;
import use_case.port.JobSearchSource;
import use_case.port.JobSource;
import use_case.port.JobSourceException;

/** Implements Finder keyword matching over complete descriptions from company boards. */
final class BoardKeywordSearch implements JobSearchSource {
  private final JobSource boards;

  BoardKeywordSearch(JobSource boards) {
    this.boards = boards;
  }

  @Override
  public JobFetchResult search(String keywords, String location) throws JobSourceException {
    return search(new SearchJobsInputData(keywords, location));
  }

  @Override
  public JobFetchResult search(SearchJobsInputData input) throws JobSourceException {
    JobFetchResult fetched = fetch(input.getBoard());
    List<String> terms = Arrays.asList(input.getQuery().toLowerCase(Locale.ROOT).split("(?U)\\s+"));
    List<JobPosting> matches =
        fetched.getJobs().stream()
            .filter(
                job -> {
                  String text =
                      (job.getTitle() + " " + job.getDescriptionText()).toLowerCase(Locale.ROOT);
                  return terms.stream().allMatch(text::contains);
                })
            .collect(Collectors.toList());
    return new JobFetchResult(matches, fetched.getSuccessfulSources(), fetched.getFailures());
  }

  private JobFetchResult fetch(String board) throws JobSourceException {
    if (board != null) {
      if (boards instanceof CompanyBoardSource) {
        return ((CompanyBoardSource) boards).fetchCompany(board);
      }
      throw new JobSourceException("This source does not support company selection.");
    }
    try {
      return boards.fetch();
    } catch (JobSourceException exception) {
      throw new JobSourceException("Search could not be completed. Please try again.");
    }
  }
}
