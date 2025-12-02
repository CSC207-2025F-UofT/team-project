package use_case.submitQuiz;

import entity.Quiz;

public interface QuizDataAccessInterface {
    Quiz findById(int quizId);
    void save(Quiz quiz);
}
