package data_access;

import entity.QuizResult;
import use_case.save_result.SaveResultDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores quiz results in a list while the app runs. Stand-in for Firestore; also used in tests.
 */
public class InMemoryQuizResultDAO implements SaveResultDataAccessInterface {
    private final List<QuizResult> results = new ArrayList<>();

    @Override
    public void save(QuizResult result) {
        results.add(result);
    }

    /** Every saved result for this name (View history will use this). */
    public List<QuizResult> findByUserName(String userName) {
        List<QuizResult> matches = new ArrayList<>();
        for (QuizResult result : results) {
            if (result.getUserName().equals(userName)) {
                matches.add(result);
            }
        }
        return matches;
    }
}
