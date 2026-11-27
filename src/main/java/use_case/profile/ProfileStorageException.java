package use_case.profile;

/** An operation on saved profile data could not be completed. */
public final class ProfileStorageException extends Exception {
  /** Describes a recoverable storage condition to the caller. */
  public ProfileStorageException(String message) {
    super(message);
  }

  /** Retains the underlying diagnostic cause for a read or write failure. */
  public ProfileStorageException(String message, Throwable cause) {
    super(message, cause);
  }
}
