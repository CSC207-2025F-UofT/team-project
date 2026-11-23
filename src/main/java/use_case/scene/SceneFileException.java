package use_case.scene;

public class SceneFileException extends Exception {
    public SceneFileException(String message) {
        super(message);
    }

    public SceneFileException(String message, Throwable cause) {
        super(message, cause);
    }
}
