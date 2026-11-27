package interface_adapter.recommendation;

import entity.JobPosting;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import use_case.recommendation.InternshipMatch;
import use_case.recommendation.RecommendationOutputBoundary;
import use_case.recommendation.RecommendationOutputData;

/** Converts matching reasons and source failures into table rows and messages. */
public final class RecommendationPresenter implements RecommendationOutputBoundary {
  private final RecommendationViewModel model;
  private final Executor ui;

  /** Injects the screen and the scheduler on which views observe state changes. */
  public RecommendationPresenter(RecommendationViewModel model, Executor ui) {
    this.model = model;
    this.ui = ui;
  }

  @Override
  public void presentResults(RecommendationOutputData result) {
    List<RecommendationRow> rows =
        result.getMatches().stream().map(RecommendationPresenter::row).collect(Collectors.toList());
    String message =
        rows.isEmpty()
            ? "No matching internships. You can edit your skills or preferences."
            : rows.size() + (rows.size() == 1 ? " matching internship" : " matching internships");
    String warning =
        result.getFailedBoards().isEmpty()
            ? ""
            : "Results are incomplete. Unavailable boards: "
                + String.join(", ", result.getFailedBoards());
    ui.execute(() -> model.display(rows, message, warning));
  }

  @Override
  public void presentError(String message) {
    ui.execute(() -> model.failed(message));
  }

  private static RecommendationRow row(InternshipMatch match) {
    JobPosting job = match.getPosting();
    return new RecommendationRow(
        job.getTitle(),
        job.getCompany(),
        job.getLocation().isBlank() ? "Not specified" : job.getLocation(),
        job.getWorkArrangement().name(),
        String.join(", ", match.getMatchedSkills()),
        job.getApplicationUrl());
  }
}
