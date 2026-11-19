package pdf_editor.view;

import org.junit.jupiter.api.Test;
import pdf_editor.use_case.FormulaEditSubmission;

import static org.junit.jupiter.api.Assertions.*;

class FormulaEditDialogDesignTest {
    @Test void modesNowRepresentHowAnExpressionIsSavedRatherThanKeyboardTranslation() {
        assertEquals(FormulaEditSubmission.Mode.CALCULATE, FormulaEditDialog.modeForSelection(0));
        assertEquals(FormulaEditSubmission.Mode.MANUAL, FormulaEditDialog.modeForSelection(1));
    }
    @Test void sourceTextIsNotTransformedInManualMode() {
        assertEquals("i^2=-1", FormulaEditSubmission.submit(" i^2=-1 ",
                FormulaEditSubmission.Mode.MANUAL, value -> {throw new AssertionError("must not calculate");}).formula());
    }
}
