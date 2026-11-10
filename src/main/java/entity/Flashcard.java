package entity;

/**
 * An entity representing a flashcard. Flashcards have a question and answer
 */
public class Flashcard {

    private String question;
    private String answer;

    /**
     * Creates a new flashcard with the given non-empty question and non-empty answer.
     * @param question the question
     * @param answer the answer
     * @throws IllegalArgumentException if the question or answer are empty
     */
    public Flashcard(String question, String answer) {
        if ("".equals(question)) {
            throw new IllegalArgumentException("Question cannot be empty");
        }
        if ("".equals(answer)) {
            throw new IllegalArgumentException("Answer cannot be empty");
        }
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
}
