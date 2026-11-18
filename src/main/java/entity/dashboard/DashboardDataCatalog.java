package entity.dashboard;

import java.time.LocalDate;
import java.util.List;

/**
 * a static collection of records for entries inside a dashboard item.
 */
public class DashboardDataCatalog {
    public record MostVisitedCountries(
            List<CountryVisit> countryVisits
    ) implements DashboardItem {}

    public record CountryVisit(
            String country,
            Integer numberOfTimesVisited,
            List<LocalDate> dates,
            List<String> landmarks
    ) {}
}
