package use_case.recommendation;

/** UC7 entry point. All criteria come from the saved profile. */
@FunctionalInterface
public interface RecommendationInputBoundary {
  /** Requests recommendations for the current student. */
  void execute();
}
