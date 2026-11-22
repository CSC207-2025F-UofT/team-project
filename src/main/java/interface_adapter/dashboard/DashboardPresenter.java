package interface_adapter.dashboard;

import entity.LocationInfoType;
import use_case.dashboard.DashboardOutputDataCatalog;
import use_case.dashboard.AnalyticsType;
import use_case.dashboard.DashboardOutputBoundary;
import use_case.dashboard.DashboardOutputData;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class DashboardPresenter implements DashboardOutputBoundary {
    private final DashboardViewModel dashboardViewModel;

    public DashboardPresenter(DashboardViewModel dashboardViewModel) {
        this.dashboardViewModel = dashboardViewModel;
    }

    public void prepareSuccessView(DashboardOutputData dashboardOutputData) {
        AnalyticsType analyticsType = dashboardOutputData.getAnalyticsType();
        switch (analyticsType) {
            case MOST_VISITED -> mostVisited(dashboardOutputData);
        }
    }
    public void prepareFailureView(String msg) {
        // TODO
    }

    private void mostVisited(DashboardOutputData data) {
        // for this analytics, there's only a table
        DashboardOutputDataCatalog.MostVisited mostVisited =
                (DashboardOutputDataCatalog.MostVisited) data.getDashboardItems().getFirst();
        List<List<String>> renderedEntries = mostVisited.placeVisits().stream()
                .map(placeVisit -> List.<String>of(placeVisit.place(),
                                placeVisit.numberOfTimesVisited().toString(),
                                String.join(", ", placeVisit.dates().stream().map(LocalDate::toString).toList()),
                                String.join(", ", placeVisit.landmarks()))
                )
                .toList();
        String typeOfPlace = resolveLocationTypeString(mostVisited.typeOfPlace());
        DashboardState currentState = buildMostVisitedState(typeOfPlace, renderedEntries);
        dashboardViewModel.setState(currentState);
        dashboardViewModel.firePropertyChanged();
    }

    private DashboardState buildMostVisitedState(String typeOfPlace, List<List<String>> renderedEntries) {
        DashboardItemState state = new DashboardStateCatalog.NestedState(List.of(
            new DashboardStateCatalog.DropdownState(
                    "most-visited",
                    "Most visited by ...",
                    List.of(
                            "Country",
                            "Province/State",
                            "County",
                            "City",
                            "Continent"
                    )
            ),
            new DashboardStateCatalog.TableState(
                    "Most Visited " + typeOfPlace,
                    List.of(typeOfPlace, "Number of Times Visited", "Dates Visited", "Landmarks Visited"),
                    renderedEntries
            )
        ));
        DashboardState currentState = dashboardViewModel.getState();
        currentState.removeAllStates();
        currentState.addItemState(state);
        return currentState;
    }

    private String resolveLocationTypeString(LocationInfoType locationInfoType) {
        Map<LocationInfoType, String> stringMap = Map.of(
                LocationInfoType.COUNTRY, "Countries",
                LocationInfoType.CITY, "Cities",
                LocationInfoType.CONTINENT, "Continents",
                LocationInfoType.STATE, "Provinces/States",
                LocationInfoType.COUNTY, "Counties"
        );
        return stringMap.get(locationInfoType);
    }
}