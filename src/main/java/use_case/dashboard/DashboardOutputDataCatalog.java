package use_case.dashboard;

import entity.LocationInfoType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * a static collection of records for entries inside a dashboard item.
 */
public class DashboardOutputDataCatalog {
    public record MostVisited(
            LocationInfoType typeOfPlace,
            List<Visit> placeVisits
    ) implements DashboardItem {}

    public record PhotoActivity(
            AnalyticsOptions.TimeGrouping grouping,
            Map<LocalDateTime, Integer> dateToCount
    ) implements DashboardItem {}

    public record Visit(
            String place,
            Integer numberOfTimesVisited,
            List<LocalDate> dates,
            List<String> landmarks
    ) {}
}
