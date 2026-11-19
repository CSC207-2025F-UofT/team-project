package use_case.import_photos;

import java.lang.RuntimeException;

public class GeocodeException extends RuntimeException {
    public GeocodeException(String message) {
        super(message);
    }

    public GeocodeException(String message, Throwable cause) {
        super(message, cause);
    }
}
