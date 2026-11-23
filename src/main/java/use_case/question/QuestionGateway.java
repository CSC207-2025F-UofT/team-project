package use_case.question;

import entity.Question;
import java.util.List;

/**
 * Returns exactly count distinct questions or throws; there is no offline fallback.
 * The current provider supports 1–3 PRACTICE questions and 1–5 EXAM questions per course.
 */
public interface QuestionGateway {
    List<Question> getQuestions(QuestionTopic topic, QuestionPurpose purpose, int count)
            throws QuestionLoadException;
}
