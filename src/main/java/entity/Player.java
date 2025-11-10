package entity;

public class Player {

    private final String userName;
    private int level;
    private int experiencePoints;
    private int questionsAnswered;
    private int questionsCorrect;

    /**
     * Creates a new Player with the given attributes.
     * @param userName the player's username
     */

    public Player(String userName) {
        this.userName = userName;
        // Assuming new players start at level 1 with 0 experience points and no questions answered
        // TODO: Implement loading existing player data from a database or file
        this.level = 1;
        this.experiencePoints = 0;
        this.questionsAnswered = 0;
        this.questionsCorrect = 0;
    }

    public String getUserName() { return userName; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public int getExperiencePoints() { return experiencePoints; }
    public void setExperiencePoints(int experiencePoints) { this.experiencePoints = experiencePoints; }

    public int getQuestionsAnswered() { return questionsAnswered; }
    public void setQuestionsAnswered(int questionsAnswered) { this.questionsAnswered = questionsAnswered; }

    public int getQuestionsCorrect() { return questionsCorrect; }
    public void setQuestionsCorrect(int questionsCorrect) { this.questionsCorrect = questionsCorrect; }

    public int getQuestionsIncorrect() {
        return questionsAnswered - questionsCorrect;
    }

    public double getAccuracy() {
        if (questionsAnswered == 0) {
            return 0.0;
        }
        return (double) questionsCorrect / questionsAnswered;
    }
}
