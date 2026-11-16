package app;

import interface_adapter.search_jobs.SearchJobsController;
import interface_adapter.search_jobs.SearchJobsPresenter;
import interface_adapter.search_jobs.SearchJobsViewModel;
import java.util.concurrent.Executor;
import use_case.port.JobSource;
import use_case.search_jobs.SearchJobsInteractor;
import view.FinderView;
import view.LinkOpener;

/** Composition root for UC4; concrete dependencies are chosen only in this outer layer. */
public final class FinderApplication {
  private FinderApplication() {}

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
    return new FinderView(controller, model, links, sourceLabel);
  }
}
