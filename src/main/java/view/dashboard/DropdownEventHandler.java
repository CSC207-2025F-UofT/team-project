package view.dashboard;

import entity.LocationInfo;
import entity.LocationInfoType;
import interface_adapter.dashboard.DashboardController;
import use_case.dashboard.AnalyticsOptions;

import java.util.Map;
import java.util.Optional;

/**
 * Handle a dropdown selection.
 */
public class DropdownEventHandler implements EventHandler<DropdownEventPacket> {

    private final DashboardController controller;

    public DropdownEventHandler(DashboardController controller) {
        this.controller = controller;
    }
    @Override
    public void handleEvent(String actionId, DropdownEventPacket eventPacket) {
        switch (actionId) {
            case "most-visited" -> handleMostVisited(eventPacket);
            case "photo-activity" -> handlePhotoActivity(eventPacket);
            default -> throw new UnsupportedOperationException("Unknown action id " + actionId);
        }
    }
    private void handleMostVisited(DropdownEventPacket eventPacket) {
        Map<String, LocationInfoType> typeMap = Map.of(
                "Country", LocationInfoType.COUNTRY,
                "Province/State", LocationInfoType.STATE,
                "County", LocationInfoType.COUNTY,
                "City", LocationInfoType.CITY,
                "Continent", LocationInfoType.CONTINENT
        );
        controller.requestMostVisitedAnalytics(Optional.of(typeMap.get(eventPacket.selected())));
    }

    private void handlePhotoActivity(DropdownEventPacket eventPacket) {
        Map<String, AnalyticsOptions.TimeGrouping> groupingMap = Map.of(
                "Year", AnalyticsOptions.TimeGrouping.YEAR,
                "Month", AnalyticsOptions.TimeGrouping.MONTH
        );
        controller.requestPhotoActivityAnalytics(Optional.of(groupingMap.get(eventPacket.selected())));
    }
}
