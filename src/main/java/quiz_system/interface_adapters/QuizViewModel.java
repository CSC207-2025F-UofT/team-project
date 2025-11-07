package quiz_system.interface_adapters;

public class QuizViewModel {
    public int quizId;
    public boolean isCompleted;
    public String status;          // "CORRECT" or "INCORRECT" or "WARNING"
    public String feedbackMessage; // "Correct Answer" or "Incorrect Answer" or "Not Answered"
}
