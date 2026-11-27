package use_case.recommendation;

/** Publishes either a completed recommendation list or a retrieval/profile failure. */
public interface RecommendationOutputBoundary {
  /** Presents suggestions and any incomplete-source warnings. */
  void presentResults(RecommendationOutputData data);

  /** Presents a reason why recommendations are unavailable. */
  void presentError(String message);
}
