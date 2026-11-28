package data_access;

import entity.Answer;
import entity.Question;
import use_case.add_question.AddQuestionDataAccessInterface;
import use_case.load_question.LoadQuestionDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * In-memory implementation of the DAO for storing question data. This implementation is for testing purposes.
 */
public class InMemoryDataAccessObject implements LoadQuestionDataAccessInterface, AddQuestionDataAccessInterface {
    List<Question> questions = new ArrayList<>();
    List<Boolean> questionUsed = new ArrayList<>();

    @Override
    public boolean existsUnusedQuestion() {
        for (Boolean used : questionUsed) {
            if (!used) {
                // there is a question that has not been used
                return true;
            }
        }
        // all questions have been used
        return false;
    }

    @Override
    public Question getNextQuestion() {
        for (int i = 0; i < questionUsed.size(); i++) {
            if (!questionUsed.get(i)) {
                questionUsed.set(i, true);
                return questions.get(i); // TODO: should this return a copy of the question instead?
            }
        }
        // program should never reach this point
        return null;
    }

    // TODO: ERIC! The following two methods I (Jacob) needed implementations for to do something, but you can absolutely change them however you want (they are from your data access interface I believe)
    @Override
    public boolean questionExists(String questionText) {
        for (Question question : questions) {
            if (question.getQuestionText().equals(questionText)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void saveQuestion(Question question) {
        this.questions.add(question);
        this.questionUsed.add(false);
    }
}
