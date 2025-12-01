package use_case.quiz;

import entity.Quiz;

public interface QuizDataAccessInterface {
    Quiz findById(int quizId);
    void save(Quiz quiz);
}
