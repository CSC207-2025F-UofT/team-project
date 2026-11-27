package use_case.search_jobs;

/** Raw query input; validation belongs to the interactor. */
public final class SearchJobsInputData {
  private final boolean loadMore;
  private final String company;
  private final String query;
  private final String location;
  private final String board;

  /** Preserves the raw query, including null or blank values for use-case validation. */
  public SearchJobsInputData(String query) {
    this(query, "");
  }

  /** Carries the optional location entered for a cross-company search. */
  public SearchJobsInputData(String query, String location) {
    this(query, location, null);
  }

  /** Captures the chosen company along with the query before scheduling retrieval. */
  public SearchJobsInputData(String query, String location, String board) {
    this(query, location, board, "", false);
  }

  private SearchJobsInputData(
      String query, String location, String board, String company, boolean loadMore) {
    this.query = query;
    this.location = location;
    this.board = board;
    this.company = company;
    this.loadMore = loadMore;
  }

  /** Keeps the job keywords separate from the optional employer and location filters. */
  public static SearchJobsInputData withFilters(String keywords, String company, String location) {
    return new SearchJobsInputData(keywords, location, null, company, false);
  }

  /** Requests another batch for the active search. */
  public static SearchJobsInputData more() {
    return new SearchJobsInputData("", "", null, "", true);
  }

  public String getCompany() {
    return company;
  }

  public boolean isLoadMore() {
    return loadMore;
  }

  public String getBoard() {
    return board;
  }

  public String getLocation() {
    return location;
  }

  public String getQuery() {
    return query;
  }
}
