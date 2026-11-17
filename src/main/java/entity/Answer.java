package entity;

import java.util.List;

/**
 * A simple entity representing a correct answer to a Question. Answers have a score, the primary answer to display,
 * and a collection of acceptable variants.
 */
public class Answer {

    private final String primaryAnswer;
    private final List<String> acceptedVariants;
    private final int score;

    /**
     * Creates a new Answer with the given non-empty primaryAnswer, the given positive score, and the given
     * acceptedVariants.
     * @param primaryAnswer the main answer associated with this object
     * @param acceptedVariants additional accepted answers tied to this object
     * @param score the number of points associated with this answer
     * @throws IllegalArgumentException if the primaryAnswer is empty or the score is non-positive
     */
    public Answer(String primaryAnswer, List<String> acceptedVariants, int score){
        if ("".equals(primaryAnswer)) {
            throw new IllegalArgumentException("Primary Answer cannot be empty");
        }
        if (score <= 0) {
            throw new IllegalArgumentException("Score cannot be non-positive");
        }

        this.primaryAnswer = primaryAnswer;
        this.acceptedVariants = acceptedVariants;
        this.score = score;
    }

    /**
     * Returns whether the provided potentialAnswer is an acceptable answer
     * @param potentialAnswer the provided answer to check for correctness
     * @return whether this answer is the primary answer or an accepted variant
     */
    public boolean isAccepted(String potentialAnswer) {
        // Check against the primary answer
        if (potentialAnswer.equalsIgnoreCase(this.primaryAnswer)){
            return true;
        }

        // Check against the variants of this answer
        for (String variant: this.acceptedVariants){
            if (potentialAnswer.equalsIgnoreCase(variant)){
                return true;
            }
        }

        // The potentialAnswer is not an acceptable answer
        return false;
    }

    /**
     * @return this Answer's associated score
     */
    public int getScore() { return this.score; }
}
