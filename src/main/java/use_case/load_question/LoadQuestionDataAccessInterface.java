package use_case.load_question;

import entity.Question;

public interface LoadQuestionDataAccessInterface {
    boolean existsUnusedQuestion();

    Question getNextQuestion();

    void setCurrentQuestion(Question question);
}
