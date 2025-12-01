package use_case.quiz;

public class SubmitQuizOutputData {
        private final int quizId;
        private final boolean isCompleted;
        private final String status;  // "CORRECT" | "INCORRECT" | "WARNING"
        private final String message; // "Correct Answer" | "Incorrect Answer" | "Question Not Answered"

    public SubmitQuizOutputData(int quizId, boolean isCompleted, String status, String message) {
            this.quizId = quizId;
            this.isCompleted = isCompleted;
            this.status = status;
            this.message = message;
        }

        public int getQuizId() { return quizId; }
        public boolean isCompleted() { return isCompleted; }
        public String getStatus() { return status; }
        public String getMessage() { return message; }
}
