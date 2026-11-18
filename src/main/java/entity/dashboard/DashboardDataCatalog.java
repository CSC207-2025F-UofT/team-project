package entity.dashboard;

import java.time.LocalDate;
import java.util.List;

/**
 * a static collection of records for entries inside a dashboard item.
 */
public class DashboardDataCatalog {
    public record MostVisitedCity(
            List<LocalDate> dates,
            List<String> tripIds,
            List<String> favouriteLandmarks
    ) {}
}
