package use_case.port;

import java.util.Objects;

/** A failed board and a safe reason, without transport exceptions or response bodies. */
public final class SourceFailure {
  /** Categories adapters can report without exposing their implementation libraries. */
  public enum Reason {
    NETWORK,
    TIMEOUT,
    HTTP_ERROR,
    INVALID_RESPONSE
  }

  private final String sourceId;
  private final Reason reason;

  /** Identifies an unavailable board and the category of failure users can act on. */
  public SourceFailure(String sourceId, Reason reason) {
    this.sourceId = Objects.requireNonNull(sourceId);
    this.reason = Objects.requireNonNull(reason);
  }

  public String getSourceId() {
    return sourceId;
  }

  public Reason getReason() {
    return reason;
  }
}
