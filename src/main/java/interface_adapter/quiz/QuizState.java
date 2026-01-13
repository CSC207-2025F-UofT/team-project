package interface_adapter.quiz;

import entity.Monster;
import entity.User;

import java.util.List;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * State object for the Quiz View.
 */
public class QuizState {
    private User user = null;
    private Monster monster = null;
    private boolean quizResult = false;
    public int quizId;
    public boolean isCompleted;
    public String status = null; // "CORRECT" or "INCORRECT" or "WARNING"
    public String feedbackMessage;
    private String questionText;
    private List<String> optionTexts;

    private static final Set<Integer> USED_QUIZ_IDS = new HashSet<>();
    private static final int MAX_QUIZ_ID = 20;
    private static final Random RANDOM = new Random();

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
    public int setQuizId() {
        // If all numbers have been used, reset so all become available again
        if (USED_QUIZ_IDS.size() >= MAX_QUIZ_ID) {
            USED_QUIZ_IDS.clear();
        }

        int id;
        do {
            id = RANDOM.nextInt(MAX_QUIZ_ID) + 1; // 1–20
        } while (USED_QUIZ_IDS.contains(id));

        USED_QUIZ_IDS.add(id);
        this.quizId = id;
        return id;
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
