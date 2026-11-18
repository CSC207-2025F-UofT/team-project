package use_case.dashboard;

import entity.LocationInfo;
import entity.Photo;
import entity.Trip;
import entity.dashboard.DashboardDataCatalog;
import use_case.list_trips.ListTripsDataAccessInterface;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Delegate to compute analytics for the specified type, and prepare output data.
 */
public class AnalyticsUseCase implements DashboardInputBoundary {
    private static final int RANKING_LIMIT = 30;
    private final ListTripsDataAccessInterface tripAccess;
    private final DashboardOutputBoundary presenter;

    public AnalyticsUseCase(ListTripsDataAccessInterface tripAccess,
                         DashboardOutputBoundary dashboardOutputBoundary) {
        this.presenter = dashboardOutputBoundary;
        this.tripAccess = tripAccess;
    }
    @Override
    public void execute(DashboardInputData dashboardInputData) {
        AnalyticsType analyticsType = dashboardInputData.getAnalyticsType();
        switch (analyticsType) {
            case MOST_VISITED_COUNTRIES: mostVisitedCountries();
        }
    }

    private void mostVisitedCountries() {
        try {
            DashboardOutputData data = new DashboardOutputData(AnalyticsType.MOST_VISITED_COUNTRIES);
            data.addDashboardItem(computeMostVisitedCountries());
            presenter.prepareSuccessView(data);
        } catch (RuntimeException e) {
            presenter.prepareFailureView(e.getMessage());
        }
    }

    private DashboardDataCatalog.MostVisitedCountries computeMostVisitedCountries() {
        List<Trip> trips = tripAccess.getAllTrips();
        Map<String, Long> numVisitsByCountry = trips.stream()
                .flatMap(trip -> tripAccess.getTripPhotos(trip.getId()).stream()
                        .map(photo -> photo.getLocation().getInfo().getCountry())
                        .collect(Collectors.toSet())
                        .stream()
                )
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Map<String, List<LocationInfo>> locationsByCountry = trips.stream()
                .flatMap(trip -> tripAccess.getTripPhotos(trip.getId()).stream())
                .map(photo -> photo.getLocation().getInfo())
                .collect(Collectors.groupingBy(LocationInfo::getCountry));

        List<DashboardDataCatalog.CountryVisits> countryVisits = locationsByCountry
                .entrySet()
                .stream()
                .map(entry -> {
                    String country = entry.getKey();
                    List<LocationInfo> locations = entry.getValue();
                    Integer numberOfTimesVisited = numVisitsByCountry.get(country).intValue();
                    List<String> landmarks = locations.stream()
                            .map(LocationInfo::getLandmark)
                            .toList();
                    List<LocalDate> dates = trips.stream()
                            .filter(trip -> getCountriesByTrip(trip).contains(country))
                            .map(trip -> trip.getStartTime().toLocalDate())
                            .toList();
                    return new DashboardDataCatalog.CountryVisits(country, numberOfTimesVisited, dates, landmarks);
                })
                .sorted(Comparator.comparingInt(DashboardDataCatalog.CountryVisits::numberOfTimesVisited).reversed())
                .limit(RANKING_LIMIT)
                .toList();

        return new DashboardDataCatalog.MostVisitedCountries(countryVisits);
    }

    private List<String> getCountriesByTrip(Trip trip) {
        return tripAccess.getTripPhotos(trip.getId()).stream()
                .map(photo -> photo.getLocation().getInfo().getCountry())
                .toList();
    }
}
