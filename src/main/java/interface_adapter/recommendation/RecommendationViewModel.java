package interface_adapter.recommendation;

import interface_adapter.ObservableViewModel;
import java.util.List;

/** Observable recommendations screen used by the presenter, controller and view. */
public final class RecommendationViewModel
    extends ObservableViewModel<RecommendationViewModel.State> {
  /** Immutable rows and status for one stage of a request. */
  public static final class State {
    private final List<RecommendationRow> rows;
    private final String message;
    private final String warning;
    private final boolean loading;

    private State(List<RecommendationRow> rows, String message, String warning, boolean loading) {
      this.rows = List.copyOf(rows);
      this.message = message;
      this.warning = warning;
      this.loading = loading;
    }

    public List<RecommendationRow> getRows() {
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
  }

  /** The screen fetches its first results when opened. */
  public RecommendationViewModel() {
    super(new State(List.of(), "", "", false));
  }

  /** Removes stale rows while a new profile and postings are being retrieved. */
  public void loading() {
    publish(new State(List.of(), "Finding recommendations…", "", true));
  }

  /** Shows the completed response with a separate partial-source warning, if present. */
  public void display(List<RecommendationRow> rows, String message, String warning) {
    publish(new State(rows, message, warning, false));
  }

  /** Leaves refresh available after a failed request. */
  public void failed(String message) {
    display(List.of(), message, "");
  }
}
