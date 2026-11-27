package interface_adapter.search_jobs;

import java.util.List;

/** Immutable display state; UI components do not need entities or transport objects. */
public final class SearchJobsState {
  private final List<Row> rows;
  private final String message;
  private final String warning;
  private final boolean loading;
  private final boolean error;
  private final boolean hasMore;

  /** Copies result rows and records the current status, warning and loading state. */
  public SearchJobsState(
      List<Row> rows, String message, String warning, boolean loading, boolean error) {
    this(rows, message, warning, loading, error, false);
  }

  /** Records whether more matches can be requested for this search. */
  public SearchJobsState(
      List<Row> rows,
      String message,
      String warning,
      boolean loading,
      boolean error,
      boolean hasMore) {
    this.hasMore = hasMore;
    this.rows = List.copyOf(rows);
    this.message = message;
    this.warning = warning;
    this.loading = loading;
    this.error = error;
  }

  /** Returns whether another batch can be requested. */
  public boolean hasMore() {
    return hasMore;
  }

  public List<Row> getRows() {
    return rows;
  }

  public String getMessage() {
    return message;
  }

  public String getWarning() {
    return warning;
  }

  public boolean isLoading() {
    return loading;
  }

  public boolean isError() {
    return error;
  }

  /** Plain display values for one matching posting. */
  public static final class Row {
    private final String id;
    private final String title;
    private final String company;
    private final String location;
    private final String applicationUrl;

    /** Retains stable identity alongside the formatted text and original application URL. */
    public Row(String id, String title, String company, String location, String applicationUrl) {
      this.id = id;
      this.title = title;
      this.company = company;
      this.location = location;
      this.applicationUrl = applicationUrl;
    }

    public String getId() {
      return id;
    }

    public String getTitle() {
      return title;
    }

    public String getCompany() {
      return company;
    }

    public String getLocation() {
      return location;
    }

    public String getApplicationUrl() {
      return applicationUrl;
    }
  }
}
