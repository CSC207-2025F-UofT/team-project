package use_case.save_result;

import entity.QuizResult;

public interface SaveResultDataAccessInterface {
    void save(QuizResult result);
}
