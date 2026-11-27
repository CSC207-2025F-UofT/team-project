package use_case;

import java.lang.RuntimeException;

public class TripAccessException extends RuntimeException {
    public TripAccessException(String message) {
        super(message);
    }

    public TripAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
