package use_case;

import java.lang.RuntimeException;

public class PhotoAccessException extends RuntimeException {
    public PhotoAccessException(String message) {
        super(message);
    }

    public PhotoAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
