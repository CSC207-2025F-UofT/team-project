package interface_adapter.evaluate;

import entity.UncalculableReason;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UncalculableTextTest {

    @Test
    void everyReasonHasWords() {
        for (UncalculableReason reason : UncalculableReason.values()) {
            assertNotEquals(UncalculableText.NO_REASON, UncalculableText.of(reason.name(), "\\sin"), reason.name());
        }
    }

    @Test
    void noReasonGetsAFallback() {
        assertEquals(UncalculableText.NO_REASON, UncalculableText.of(null, null));
    }

    @Test
    void wordsStayTheSame() {
        assertEquals("Number too big", UncalculableText.of("TOO_BIG", null));
        assertEquals("sin⁻¹ is ambiguous", UncalculableText.of("INVERSE_AMBIGUOUS", "\\sin"));
    }
}
