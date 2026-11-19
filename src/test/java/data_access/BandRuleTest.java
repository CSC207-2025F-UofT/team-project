package data_access;

import org.junit.jupiter.api.Test;

import static data_access.InkPictures.assertSamePixels;
import static data_access.InkPictures.fraction;
import static data_access.InkPictures.oneLine;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BandRuleTest {

    @Test
    void wholeInkPutsAFractionInOneLine() {
        assertEquals(128, FormulaImagePreprocessor.prepare(fraction(), BandRule.WHOLE_INK).getHeight());
    }

    @Test
    void trainingsRuleStillSizesOffTheTallestBand() {
        assertTrue(FormulaImagePreprocessor.prepare(fraction(), BandRule.TALLEST_BAND).getHeight() > 128);
    }

    @Test
    void plainPrepareIsStillTrainingsRule() {
        assertSamePixels(FormulaImagePreprocessor.prepare(fraction()),
                FormulaImagePreprocessor.prepare(fraction(), BandRule.TALLEST_BAND));
    }

    @Test
    void oneLineComesOutTheSameEitherWay() {
        assertSamePixels(FormulaImagePreprocessor.prepare(oneLine(), BandRule.TALLEST_BAND),
                FormulaImagePreprocessor.prepare(oneLine(), BandRule.WHOLE_INK));
    }
}
