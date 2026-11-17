package use_case.evaluate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuestionCheckInteractorTest {
    private final QuestionCheckInputBoundary check = new QuestionCheckInteractor();

    @Test
    void comparisonsWithBothSidesCount() {
        assertTrue(check.isQuestion("2x>5"));
        assertTrue(check.isQuestion("x<3"));
        assertTrue(check.isQuestion("x\\neq3"));
        assertTrue(check.isQuestion("x\\leq1"));
        assertTrue(check.isQuestion("x\\ge1"), "alias still a relation");
        assertTrue(check.isQuestion("\\sqrt{2}\\in\\mathbb{Q}"));
        assertTrue(check.isQuestion("f(x)>2"));
        assertTrue(check.isQuestion("1+1=2"));
    }

    @Test
    void halfWrittenWaits() {
        assertFalse(check.isQuestion("2x>"));
        assertFalse(check.isQuestion(">5"));
        assertFalse(check.isQuestion("1<x<"), "still writing the 2nd side");
        assertFalse(check.isQuestion("1+1"));
    }

    @Test
    void relationsTuckedInsideDontCount() {
        assertFalse(check.isQuestion("\\sum_{n=0}^{5}n"));
        assertTrue(check.isQuestion("\\sum_{n=0}^{5}n>3"), "the outer > still does");
    }

    @Test
    void junkIsJustNo() {
        assertFalse(check.isQuestion(null));
        assertFalse(check.isQuestion(""));
        assertFalse(check.isQuestion("\\text{hi}>1"));
        assertFalse(check.isQuestion("2x>5$"));
    }
}
