package photo;

import java.lang.RuntimeException;

public class PhotoHandlerException extends RuntimeException {
    public PhotoHandlerException(String message) {
        super(message);
    }

    public PhotoHandlerException(String message, Throwable cause) {
        super(message, cause);
    }
}
