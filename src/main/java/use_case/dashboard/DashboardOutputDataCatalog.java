package use_case.dashboard;

import entity.LocationInfoType;

import java.time.LocalDate;
import java.util.List;

/**
 * a static collection of records for entries inside a dashboard item.
 */
public class DashboardOutputDataCatalog {
    public record MostVisited(
            LocationInfoType typeOfPlace,
            List<Visit> placeVisits
    ) implements DashboardItem {}

    public record Visit(
            String place,
            Integer numberOfTimesVisited,
            List<LocalDate> dates,
            List<String> landmarks
    ) {}
}
