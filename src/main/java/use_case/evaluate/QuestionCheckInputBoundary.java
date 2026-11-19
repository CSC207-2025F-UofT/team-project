package use_case.evaluate;

// is this a finished comparison (2x>5), so it's worth offering
public interface QuestionCheckInputBoundary {
    boolean isQuestion(String latex);
}
