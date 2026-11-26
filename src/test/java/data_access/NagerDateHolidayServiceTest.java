package data_access;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NagerDateHolidayServiceTest {
    @Test
    void canadaDayIsReturnedForCanada() {
        NagerDateHolidayService service = new NagerDateHolidayService();

        Optional<String> result = service.getHolidayName(LocalDate.of(2026, 7, 1), "CA");

        assertEquals(Optional.of("Canada Day"), result);
    }
}
