package entity;

/**
 * A simple entity representing a Team. Teams have a name, and a score.
 */
public class Team {

    private final String name;
    private int score;

    /**
     * Creates a new Team with the given non-empty name.
     * @param name the name of the team
     * @throws IllegalArgumentException if the name is empty.
     */
    public Team(String name) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("name cannot be empty");
        }

        this.name = name;
        this.score = 0;
    }

    /**
     * Increment the team's score by the provided number of points
     * @param pointsToAdd the number of points to add to the score
     * @throws IllegalArgumentException if pointsToAdd is negative
     */
    public void addPoints(int pointsToAdd) {
        if (pointsToAdd < 0){
            throw new IllegalArgumentException("pointsToAdd cannot be negative");
        }

        this.score += pointsToAdd;
    }

    /**
     * @return this Team's name
     */
    public String getName() { return name; }

    /**
     * @return this Team's score
     */
    public int getScore() { return score; }
}
