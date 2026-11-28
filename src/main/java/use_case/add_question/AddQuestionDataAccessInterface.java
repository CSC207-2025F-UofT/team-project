package use_case.add_question;
import entity.Question;
/**
 * Data Access Interface for saving and checking questions.
 */
public interface AddQuestionDataAccessInterface {
    /**
     * Checks if a question with the given text already exists in the database.
     * @param questionText the question text to check
     * @return true if it exists, false otherwise
     */
    boolean questionExists(String questionText);
    /**
     * Saves a new Question entity to the persistent database.
     * @param question the Question entity to save
     */
    void saveQuestion(Question question);
}
