package entity;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class QuestionSessionTest {
    private Question question(String id, int correct) {
        return new Question(id, "Question", List.of("A", "B", "C", "D"), correct, null);
    }

    @Test void validatesAndDefensivelyCopiesQuestions() {
        assertThrows(IllegalArgumentException.class, () -> question("", 0));
        assertThrows(IllegalArgumentException.class, () -> question("q", 4));
        Question q = question("q", 1);
        assertThrows(UnsupportedOperationException.class, () -> q.getOptions().add("E"));
        assertTrue(q.getExplanation().isEmpty());
        assertThrows(IllegalArgumentException.class, () -> new QuestionSession(List.of(q, q)));
    }

    @Test void examAnswersCanBeEditedAndHintDoesNotChangeScore() {
        QuestionSession session = new QuestionSession(List.of(question("1", 1), question("2", 0)));
        assertFalse(session.isComplete());
        assertThrows(IllegalArgumentException.class, () -> session.answer(0, -1));
        session.answer(0, 0);
        session.answer(0, 1);
        session.answer(1, 0);
        assertTrue(session.isComplete());
        assertEquals(2, session.getCorrectCount());
        assertEquals(0, session.useHint(0));
        assertThrows(IllegalStateException.class, () -> session.useHint(1));
        assertEquals(2, session.getCorrectCount());
    }

    @Test void fiveQuestionExamRequiresAllAnswersAndStillAllowsOnlyOneHint() {
        QuestionSession session = new QuestionSession(List.of(question("1", 0), question("2", 1),
                question("3", 2), question("4", 3), question("5", 0)));
        for (int index = 0; index < 4; index++) { session.answer(index, index); }
        assertFalse(session.isComplete());
        assertEquals(1, session.useHint(4));
        assertFalse(session.isComplete());
        session.answer(4, 0);
        assertTrue(session.isComplete());
        assertEquals(5, session.getCorrectCount());
        assertThrows(IllegalStateException.class, () -> session.useHint(0));
    }
}
