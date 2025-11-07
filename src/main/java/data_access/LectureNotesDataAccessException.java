package data_access;

public class LectureNotesDataAccessException extends Exception {

    public LectureNotesDataAccessException(String message) {
        super(message);
    }

    public LectureNotesDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
