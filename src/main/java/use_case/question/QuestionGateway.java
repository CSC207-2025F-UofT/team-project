package use_case.question;

import entity.Question;
import java.util.List;

/** Returns exactly count distinct questions or throws; there is no offline fallback. */
public interface QuestionGateway {
    List<Question> getQuestions(QuestionTopic topic, QuestionPurpose purpose, int count)
            throws QuestionLoadException;
}
