package quiz_system.usecase;

import quiz_system.entity.Quiz;

public interface QuizRepository {
    Quiz findById(int quizId);
    void save(Quiz quiz);
}
