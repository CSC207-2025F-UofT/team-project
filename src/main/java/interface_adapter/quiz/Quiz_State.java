package interface_adapter.quiz;

import entity.Monster;
import entity.User;

import java.util.List;

/**
 * State object for the Quiz View.
 */
public class Quiz_State {
    private User user = null;
    private Monster monster = null;
    private boolean quizResult = false;
    public int quizId;
    public boolean isCompleted;
    public String status = null; // "CORRECT" or "INCORRECT" or "WARNING"
    public String feedbackMessage;
    private String questionText;
    private List<String> optionTexts;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public int getQuizId() {
        return quizId;
    }
    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }
    public boolean isCompleted() {
        return quizResult;
    }

    public void setCompleted(boolean quizResult) {
        this.quizResult = quizResult;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getFeedbackMessage() {
        return feedbackMessage;
    }
    public void setFeedbackMessage(String feedbackMessage) {
        this.feedbackMessage = feedbackMessage;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<String> getOptionTexts() {
        return optionTexts;
    }

    public void setOptionTexts(List<String> optionTexts) {
        this.optionTexts = optionTexts;
    }
}
