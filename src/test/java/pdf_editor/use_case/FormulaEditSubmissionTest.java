package pdf_editor.use_case;

import org.junit.jupiter.api.Test;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class FormulaEditSubmissionTest {
    @Test void manualStoresTheEquationExactlyAndNeverInvokesCalculator() {
        AtomicInteger evaluations = new AtomicInteger();
        var saved = FormulaEditSubmission.submit(" 1+1=3 ", FormulaEditSubmission.Mode.MANUAL,
                expression -> {evaluations.incrementAndGet();throw new AssertionError("should not evaluate");});
        assertEquals("1+1=3", saved.formula());
        assertEquals("", saved.result());
        assertEquals(0, evaluations.get());
    }
    @Test void manualAllowsUnsupportedComplexNumberSyntax() {
        var saved = FormulaEditSubmission.submit("\\sqrt{-1}=i", FormulaEditSubmission.Mode.MANUAL, null);
        assertEquals("\\sqrt{-1}=i", saved.formula());
        assertEquals("", saved.result());
    }
    @Test void calculateDelegatesOnceAndPreservesBothValues() {
        AtomicInteger evaluations = new AtomicInteger();
        var saved = FormulaEditSubmission.submit("2+3", FormulaEditSubmission.Mode.CALCULATE,
                expression -> {evaluations.incrementAndGet(); return " 5 ";});
        assertEquals("2+3", saved.formula());
        assertEquals("5", saved.result());
        assertEquals(1, evaluations.get());
    }
    @Test void blankInputIsRejectedInEitherMode() {
        for (var mode : FormulaEditSubmission.Mode.values()) {
            assertThrows(IllegalArgumentException.class,
                    () -> FormulaEditSubmission.submit(" \t ", mode, value -> "any"));
        }
    }
    @Test void manualDoesNotNeedAnEvaluatorButCalculatedModeDoes() {
        assertEquals("", FormulaEditSubmission.submit("a=b", FormulaEditSubmission.Mode.MANUAL, null).result());
        assertThrows(NullPointerException.class,
                () -> FormulaEditSubmission.submit("a=b", FormulaEditSubmission.Mode.CALCULATE, null));
    }
}
