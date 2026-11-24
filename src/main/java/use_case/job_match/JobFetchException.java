package use_case.job_match;

/**
 * Thrown by a job data access object when jobs could not be fetched.
 */
public class JobFetchException extends RuntimeException {

    public JobFetchException(String message) {
        super(message);
    }

    public JobFetchException(String message, Throwable cause) {
        super(message, cause);
    }
}
