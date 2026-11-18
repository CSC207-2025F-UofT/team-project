package entity.dashboard;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * a static collection of records for entries inside a dashboard item.
 */
public class DashboardDataCatalog {
    public record MostVisitedCountries(
            List<CountryVisits> countryVisits
    ) implements DashboardItem {}

    public record CountryVisits(
            String country,
            Integer numberOfTimesVisited,
            List<LocalDate> dates,
            List<String> landmarks
    ) {}
}
