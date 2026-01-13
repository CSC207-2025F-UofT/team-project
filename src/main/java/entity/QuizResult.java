package entity;

public final class QuizResult {
    public enum Status { CORRECT, INCORRECT, WARNING }

    private final Status status;
    private final String message;

    private QuizResult(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public static QuizResult correct(String msg) {
        return new QuizResult(Status.CORRECT, msg);
    }

    public static QuizResult incorrect(String msg) {
        return new QuizResult(Status.INCORRECT, msg);
    }

    public static QuizResult warning(String msg) {
        return new QuizResult(Status.WARNING, msg);
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
