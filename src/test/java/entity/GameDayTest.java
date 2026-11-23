package entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GameDayTest {
    private static final LocalDate START = LocalDate.of(2025, 9, 2);

    @Test
    void dayOneMatchesStartDate() {
        assertEquals(START, new GameDay(1, START).getRealDate());
    }

    @Test
    void nextAdvancesDayNumberAndRealDate() {
        GameDay next = new GameDay(1, START).next();
        assertEquals(2, next.getDayNumber());
        assertEquals(START.plusDays(1), next.getRealDate());
        assertEquals(START, next.getStartDate());
    }

    @Test
    void rejectsDayNumberBelowOne() {
        assertThrows(IllegalArgumentException.class, () -> new GameDay(0, START));
    }
}
