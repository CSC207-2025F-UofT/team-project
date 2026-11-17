package view.canvas;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FormulaIconsTest {
    @Test
    void goodLatexTypesets() {
        assertNotNull(FormulaIcons.inline("\\frac{1}{2}+2^{3}"));
        assertTrue(FormulaIcons.inline("y=x^{2}").getIconWidth() > 0);
    }

    @Test
    void badOrBlankGivesNullSoTheCardFallsBackToText() {
        assertNull(FormulaIcons.inline("\\left("));
        assertNull(FormulaIcons.inline(""));
        assertNull(FormulaIcons.inline(null));
    }
}
