package data_access;

import entity.Answer;
import entity.Question;
import use_case.load_question.LoadQuestionDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class DataAccessObject implements LoadQuestionDataAccessInterface {

    // TEMPORARY IMPLEMENTATION FOR TESTING PURPOSES
    @Override
    public boolean existsUnusedQuestion() {
        return true;
    }

    // TEMPORARY IMPLEMENTATION FOR TESTING PURPOSES
    @Override
    public Question getNextQuestion() {
        List<Answer> answers = new ArrayList<>();
        answers.add(new Answer("Balloon", null, 44));
        answers.add(new Answer("Tire", null, 25));
        answers.add(new Answer("Beach ball", null, 10));
        answers.add(new Answer("Ego", null, 7));
        answers.add(new Answer("Raft", null, 5));
        return new Question("Name something that can be inflated or deflated", answers);
    }

    // TEMPORARY IMPLEMENTATION FOR TESTING PURPOSES
    @Override
    public void setCurrentQuestion(Question question) {
        return;
    }
}
