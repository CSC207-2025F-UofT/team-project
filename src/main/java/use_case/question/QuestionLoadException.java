package use_case.question;

public final class QuestionLoadException extends Exception {
    public QuestionLoadException(String message) { super(message); }
    public QuestionLoadException(String message, Throwable cause) { super(message, cause); }
}
