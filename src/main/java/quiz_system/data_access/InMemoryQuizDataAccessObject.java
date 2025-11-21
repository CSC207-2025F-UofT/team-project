package quiz_system.data_access;

import quiz_system.usecase.QuizDataAccessInterface;
import quiz_system.entity.Quiz;

import java.util.HashMap;
import java.util.Map;

public class InMemoryQuizDataAccessObject implements QuizDataAccessInterface {
    private final Map<Integer, Quiz> store = new HashMap<>();

    @Override
    public Quiz findById(int quizId) {
        return store.get(quizId);
    }

    @Override
    public void save(Quiz quiz) {
        store.put(quiz.getQuizId(), quiz);
    }

     // helper for preloading quizzes during tests
    public void put(Quiz quiz) {
        save(quiz);
    }
}
