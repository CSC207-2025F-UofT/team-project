package entity;

import java.util.List;

/**
 * A simple entity representing a Trivia Question. Questions have their main text, and a collection of correct Answers.
 */
public class Question {

    private final String questionText;
    private final List<Answer> correctAnswers;

    /**
     * Creates a new Question  with the given non-empty questionText, and the given nonempty acceptedVariants.
     * @param questionText the main text of the question
     * @param correctAnswers the acceptable answers to the question
     * @throws IllegalArgumentException if the questionText or the correctAnswers are empty.
     */
    public Question(String questionText, List<Answer> correctAnswers){
        if ("".equals(questionText)) {
            throw new IllegalArgumentException("questionText cannot be empty");
        }
        if (correctAnswers.isEmpty()) {
            throw new IllegalArgumentException("correctAnswers cannot be empty");
        }

        this.questionText = questionText;
        this.correctAnswers = correctAnswers;
    }

    /**
     * Returns the score associated with the provided answer, or 0 if the answer is not correct.
     * @param potentialAnswer the provided answer to check for correctness
     * @return the score associated with the provided answer, or 0 if the answer is not correct.
     */
    public int verifyAnswer(String potentialAnswer) {
        // check provided answer against all correct answers
        for (Answer answer: this.correctAnswers) {
            if (answer.isAccepted(potentialAnswer)) {
                return answer.getScore();
            }
        }

        // this answer does not match with any of the correct answers
        return 0;
    }

    /**
     * @return this Question's associated text
     */
    public String getQuestionText() { return this.questionText; }
}
