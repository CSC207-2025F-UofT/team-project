package quiz_system.data_access;

import quiz_system.usecase.QuizRepository;
import quiz_system.entity.Quiz;

import java.util.HashMap;
import java.util.Map;

public class InMemoryQuizRepository implements QuizRepository{
    private final Map<Integer, Quiz> store = new HashMap<>();

    @Override
    public Quiz findById(int quizId) {
        return store.get(quizId);
    }

    @Override
    public void save(Quiz quiz) {
        store.put(quiz.getQuizId(), quiz);
    }

     // helper for preloading quizzes during tests/demo
    public void put(Quiz quiz) {
        save(quiz);
    }
}
