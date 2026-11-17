package data_access;

/** Thrown when stored data cannot be read or written. */
public class DataAccessException extends RuntimeException {

  /**
   * Creates the exception.
   *
   * @param message what went wrong
   * @param cause the underlying error
   */
  public DataAccessException(String message, Throwable cause) {
    super(message, cause);
  }
}
