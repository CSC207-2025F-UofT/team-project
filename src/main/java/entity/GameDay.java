package entity;

import java.time.LocalDate;

/**
 * The current game day and the real date it is matched to. Immutable: resting produces the next GameDay.
 */
public class GameDay {
    private final int dayNumber;
    private final LocalDate startDate;

    /**
     * @param dayNumber the day number, starting at 1
     * @param startDate the real date matched to day 1
     */
    public GameDay(int dayNumber, LocalDate startDate) {
        if (dayNumber < 1) {
            throw new IllegalArgumentException("dayNumber must be at least 1");
        }
        this.dayNumber = dayNumber;
        this.startDate = startDate;
    }

    public LocalDate getRealDate() {
        return startDate.plusDays(dayNumber - 1L);
    }

    public GameDay next() {
        return new GameDay(dayNumber + 1, startDate);
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}
