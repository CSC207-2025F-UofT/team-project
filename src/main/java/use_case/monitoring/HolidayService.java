package use_case.monitoring;

import java.time.LocalDate;
import java.util.Optional;

public interface HolidayService {
    Optional<String> getHolidayName(LocalDate date, String countryCode);
}
