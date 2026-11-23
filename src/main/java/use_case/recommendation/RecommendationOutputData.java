package use_case.recommendation;

import java.util.List;

/** The ordered matches and any boards that were unavailable during a successful retrieval. */
public final class RecommendationOutputData {
  private final List<InternshipMatch> matches;
  private final List<String> failedBoards;

  /** Copies both lists so results remain stable while the UI displays them. */
  public RecommendationOutputData(List<InternshipMatch> matches, List<String> failedBoards) {
    this.matches = List.copyOf(matches);
    this.failedBoards = List.copyOf(failedBoards);
  }

  public List<InternshipMatch> getMatches() {
    return matches;
  }

  public List<String> getFailedBoards() {
    return failedBoards;
  }
}
