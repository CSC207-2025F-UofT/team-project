package data_access;

import entity.Answer;
import entity.Question;
import use_case.load_question.LoadQuestionDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class DataAccessObject implements LoadQuestionDataAccessInterface {
    public DataAccessObject() {

    }

    // TEMPORARY IMPLEMENTATION FOR TESTING PURPOSES
    @Override
    public boolean existsUnusedQuestion() {
        return true;
    }

    // TEMPORARY IMPLEMENTATION FOR TESTING PURPOSES
    @Override
    public Question getNextQuestion() {
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Answer 1", null, 10));
        answers.add(new Answer("Answer 2", null, 10));
        answers.add(new Answer("Answer 3", null, 10));
        answers.add(new Answer("Answer 4", null, 10));
        answers.add(new Answer("Answer 5", null, 10));
        return new Question("Test Question", answers);
    }

    // TEMPORARY IMPLEMENTATION FOR TESTING PURPOSES
    @Override
    public void setCurrentQuestion(Question question) {
        return;
    }
}
