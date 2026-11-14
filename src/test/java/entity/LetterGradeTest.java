package entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LetterGradeTest {

    @Test
    void fromPercentageUsesBoundaries() {
        assertEquals(LetterGrade.A_PLUS, LetterGrade.fromPercentage(100));
        assertEquals(LetterGrade.A_PLUS, LetterGrade.fromPercentage(90));
        assertEquals(LetterGrade.A, LetterGrade.fromPercentage(89.9));
        assertEquals(LetterGrade.B_MINUS, LetterGrade.fromPercentage(70));
        assertEquals(LetterGrade.D_MINUS, LetterGrade.fromPercentage(50));
        assertEquals(LetterGrade.F, LetterGrade.fromPercentage(49.9));
        assertEquals(LetterGrade.F, LetterGrade.fromPercentage(0));
    }

    @Test
    void fromPercentageRejectsOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> LetterGrade.fromPercentage(-1));
        assertThrows(IllegalArgumentException.class, () -> LetterGrade.fromPercentage(100.5));
    }

    @Test
    void fromLabel() {
        assertEquals(LetterGrade.B_PLUS, LetterGrade.fromLabel(" b+ "));
        assertEquals("A-", LetterGrade.A_MINUS.getLabel());
        assertThrows(IllegalArgumentException.class, () -> LetterGrade.fromLabel("E"));
    }
}
