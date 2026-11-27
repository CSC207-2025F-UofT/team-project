package use_case.port;

/** Retrieval could not provide usable results; this is not a successful empty search. */
public final class JobSourceException extends Exception {
  /** Creates a domain-level failure without exposing HTTP or JSON exception types. */
  public JobSourceException(String message) {
    super(message);
  }
}
