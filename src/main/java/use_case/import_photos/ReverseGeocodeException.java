package use_case.import_photos;

import java.lang.RuntimeException;

public class ReverseGeocodeException extends RuntimeException {
    public ReverseGeocodeException(String message) {
        super(message);
    }

    public ReverseGeocodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
