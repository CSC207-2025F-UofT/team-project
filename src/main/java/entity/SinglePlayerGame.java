package entity;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class SinglePlayerGame {
    private final Player player;
    private final StudySet studySet;
    private final List<Question> questions;
    private int score;
    private int correctAnswers;
    private int totalQuestions;
    private double averageResponseTime;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private final int timePerQuestion; // seconds
    private final boolean shuffleEnabled;
    private final int fixedTimePerQuestion;

    // Constructor
    public SinglePlayerGame(Player player, StudySet studySet, int timerPerQuestion, boolean shuffleEnabled) {
        this.player = player;
        this.studySet = studySet;
        this.questions = studySet.getQuestions();
        // this will come from StudySet file
        this.timePerQuestion = timerPerQuestion;
        this.shuffleEnabled = shuffleEnabled;
        this.totalQuestions = questions.size();
        this.score = 0;
        this.correctAnswers = 0;
        this.fixedTimePerQuestion = 10;
        if (shuffleEnabled) {
            Collections.shuffle(this.questions);
        }
    }

    // Getters
    public int getScore() { return score; }
    public int getCorrectAnswers() { return correctAnswers; }
    public double getAverageResponseTime() { return averageResponseTime; }
    public StudySet getStudySet() { return studySet; }
    public Player getPlayer() { return player; }

}
