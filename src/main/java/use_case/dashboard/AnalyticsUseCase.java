package use_case.dashboard;

import entity.LocationInfo;
import entity.LocationInfoType;
import entity.Trip;
import use_case.PhotoAccessInterface;
import use_case.TripAccessInterface;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Delegate to compute analytics for the specified type, and prepare output data.
 */
public class AnalyticsUseCase implements DashboardInputBoundary {
    private static final int RANKING_LIMIT = 30;
    private final TripAccessInterface tripAccess;
    private final PhotoAccessInterface photoAccess;
    private final DashboardOutputBoundary presenter;

    public AnalyticsUseCase(TripAccessInterface tripAccess,
                            PhotoAccessInterface photoAccess,
                            DashboardOutputBoundary dashboardOutputBoundary) {
        this.presenter = dashboardOutputBoundary;
        this.photoAccess = photoAccess;
        this.tripAccess = tripAccess;
    }
    @Override
    public void execute(DashboardInputData dashboardInputData) {
        AnalyticsType analyticsType = dashboardInputData.getAnalyticsType();
        switch (analyticsType) {
            case MOST_VISITED: mostVisited(dashboardInputData);
        }
    }

    private void mostVisited(DashboardInputData dashboardInputData) {
        Optional<LocationInfoType> locationInfoType = ((DashboardInputDataCatalog.MostVisitedInputData)dashboardInputData).locationInfoType();
        DashboardOutputData data = new DashboardOutputData(AnalyticsType.MOST_VISITED);
        LocationInfoType locationType = locationInfoType.orElse(LocationInfoType.COUNTRY); // default location type
        data.addDashboardItem(computeMostVisited(locationType));
        presenter.prepareSuccessView(data);
    }

    private DashboardOutputDataCatalog.MostVisited computeMostVisited(LocationInfoType locationInfoType) {
        Function<LocationInfo, String> locationSelector = info -> info.getType(locationInfoType);
        List<Trip> trips = tripAccess.getTrips();
        Map<String, Long> numVisitsByPlace = trips.stream()
                .flatMap(trip -> photoAccess.getPhotosByTripId(trip.getId()).stream()
                        .map(photo -> locationSelector.apply(photo.getLocation().getInfo()))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toSet())
                        .stream()
                )
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Map<String, List<LocationInfo>> locationsByPlace = trips.stream()
                .flatMap(trip -> photoAccess.getPhotosByTripId(trip.getId()).stream())
                .map(photo -> photo.getLocation().getInfo())
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(locationSelector));

        List<DashboardOutputDataCatalog.Visit> visits = locationsByPlace
                .entrySet()
                .stream()
                .map(entry -> {
                    String place = entry.getKey();
                    List<LocationInfo> locations = entry.getValue();
                    Integer numberOfTimesVisited = numVisitsByPlace.get(place).intValue();
                    List<String> landmarks = locations.stream()
                            .map(LocationInfo::getLandmark)
                            .filter(Objects::nonNull)
                            .toList();
                    List<LocalDate> dates = trips.stream()
                            .filter(trip -> getPlacesByTrip(trip, locationSelector).contains(place))
                            .map(Trip::getStartTime)
                            .filter(Objects::nonNull)
                            .map(LocalDateTime::toLocalDate)
                            .toList();
                    return new DashboardOutputDataCatalog.Visit(place, numberOfTimesVisited, dates, landmarks);
                })
                .sorted(Comparator.comparingInt(DashboardOutputDataCatalog.Visit::numberOfTimesVisited).reversed())
                .limit(RANKING_LIMIT)
                .toList();

        return new DashboardOutputDataCatalog.MostVisited(locationInfoType, visits);
    }

    private List<String> getPlacesByTrip(Trip trip, Function<LocationInfo, String> locationSelector) {
        return photoAccess.getPhotosByTripId(trip.getId()).stream()
                .map(photo -> locationSelector.apply(photo.getLocation().getInfo()))
                .toList();
    }
}
