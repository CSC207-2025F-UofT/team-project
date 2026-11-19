package interface_adapter.dashboard;

import entity.dashboard.DashboardDataCatalog;
import entity.dashboard.DashboardItem;
import use_case.dashboard.AnalyticsType;
import use_case.dashboard.DashboardInputBoundary;
import use_case.dashboard.DashboardOutputBoundary;
import use_case.dashboard.DashboardOutputData;
import view.dashboard.DashboardView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DashboardPresenter implements DashboardOutputBoundary {
    private final DashboardViewModel dashboardViewModel;

    public DashboardPresenter(DashboardViewModel dashboardViewModel) {
        this.dashboardViewModel = dashboardViewModel;
    }

    public void prepareSuccessView(DashboardOutputData dashboardOutputData) {
        AnalyticsType analyticsType = dashboardOutputData.getAnalyticsType();
        switch (analyticsType) {
            case MOST_VISITED_COUNTRIES -> mostVisitedCountries(dashboardOutputData);
        }
    }
    public void prepareFailureView(String msg) {
        // TODO
    }

    private void mostVisitedCountries(DashboardOutputData data) {
        // for this analytics, there's only a table
        DashboardDataCatalog.MostVisitedCountries mostVisitedCountries =
                (DashboardDataCatalog.MostVisitedCountries) data.getDashboardItems().get(0);
        List<List<String>> renderedEntries = mostVisitedCountries.countryVisits().stream()
                .map(country -> List.<String>of(country.country(), country.numberOfTimesVisited().toString(),
                        String.join(", ", country.dates().stream().map(LocalDate::toString).toList()))
                )
                .toList();
        DashboardItemState state = new DashboardStateCatalog.TableState(
                "Most Visited Countries",
                List.of("Country", "Number of Times Visited", "Dates Visited", "Landmarks Visited"),
                renderedEntries
        );
        DashboardState currentState = dashboardViewModel.getState();
        currentState.removeAllStates();
        currentState.addItemState(state);
        dashboardViewModel.setState(currentState);
        dashboardViewModel.firePropertyChanged();
    }


}