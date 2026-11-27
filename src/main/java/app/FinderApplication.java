package app;

import interface_adapter.search_jobs.SearchJobsController;
import interface_adapter.search_jobs.SearchJobsPresenter;
import interface_adapter.search_jobs.SearchJobsViewModel;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import use_case.port.CompanyBoardSource;
import use_case.port.JobSource;
import use_case.port.PagedJobSearchSource;
import use_case.search_jobs.SearchJobsInteractor;
import view.FinderView;
import view.LinkOpener;

/** Composition root for UC4; concrete dependencies are chosen only in this outer layer. */
public final class FinderApplication {
  private FinderApplication() {}

  /** Assembles the company-board and Jooble tabs with independent search state. */
  public static JTabbedPane createLiveView(
      JobSource boards,
      Optional<PagedJobSearchSource> jooble,
      Executor worker,
      Executor ui,
      LinkOpener links,
      String sourceLabel) {
    JTabbedPane tabs = new JTabbedPane();
    tabs.addTab("Company boards", createView(boards, worker, ui, links, sourceLabel));
    tabs.addTab("Jooble Canada", joobleView(jooble, worker, ui, links));
    return tabs;
  }

  private static JComponent joobleView(
      Optional<PagedJobSearchSource> source, Executor worker, Executor ui, LinkOpener links) {
    if (source.isEmpty()) {
      return new JLabel(
          "<html>To search Jooble Canada, set JOOBLE_API_KEY"
              + "<br>in your run configuration and restart Finder.</html>",
          SwingConstants.CENTER);
    }
    SearchJobsViewModel model = new SearchJobsViewModel();
    SearchJobsPresenter presenter = new SearchJobsPresenter(model, ui);
    SearchJobsController controller =
        new SearchJobsController(
            new SearchJobsInteractor(source.get(), presenter), model, worker, ui);
    return new FinderView(
        controller,
        model,
        links,
        "Search internships listed by Jooble",
        FinderView.Source.JOOBLE,
        Map.of());
  }

  /**
   * Assembles the same search engine for production and integration tests.
   *
   * @param source shared job retrieval implementation
   * @param worker executor for blocking retrieval
   * @param ui executor for display updates
   * @param links platform application-link opener
   * @param sourceLabel user-facing description of configured sources
   * @return assembled Finder panel; construct and use it on the Swing event-dispatch thread
   */
  public static FinderView createView(
      JobSource source, Executor worker, Executor ui, LinkOpener links, String sourceLabel) {
    SearchJobsViewModel model = new SearchJobsViewModel();
    SearchJobsPresenter presenter = new SearchJobsPresenter(model, ui);
    SearchJobsInteractor interactor = new SearchJobsInteractor(source, presenter);
    SearchJobsController controller = new SearchJobsController(interactor, model, worker, ui);
    Map<String, String> names =
        source instanceof CompanyBoardSource
            ? ((CompanyBoardSource) source).getCompanies()
            : Map.of();
    return new FinderView(controller, model, links, sourceLabel, FinderView.Source.BOARDS, names);
  }
}
