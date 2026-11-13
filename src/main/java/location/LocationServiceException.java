package location;

import java.lang.RuntimeException;

public class LocationServiceException extends RuntimeException {
    public LocationServiceException(String message) {
        super(message);
    }

    public LocationServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
