package interface_adapter.recommendation;

import interface_adapter.UiTasks;
import use_case.recommendation.RecommendationInputBoundary;

/** Refreshes from the latest saved profile when recommendations are opened or refreshed. */
public final class RecommendationController {
  private final RecommendationInputBoundary recommend;
  private final RecommendationViewModel model;
  private final UiTasks tasks;
  private boolean refreshPending;

  /** Coalesces repeated tab visits during a fetch into one follow-up refresh. */
  public RecommendationController(
      RecommendationInputBoundary recommend, RecommendationViewModel model, UiTasks tasks) {
    this.recommend = recommend;
    this.model = model;
    this.tasks = tasks;
    model.addPropertyChangeListener(
        event -> {
          if (refreshPending && !model.getState().isLoading()) {
            refreshPending = false;
            execute();
          }
        });
  }

  /** Called on the UI thread; retrieval and matching run on the worker. */
  public void execute() {
    if (model.getState().isLoading()) {
      refreshPending = true;
      return;
    }
    model.loading();
    tasks.run(
        recommend::execute, () -> model.failed("Could not get recommendations. Please refresh."));
  }
}
