package view.dashboard;

import entity.LocationInfo;
import entity.LocationInfoType;
import interface_adapter.dashboard.DashboardController;

import java.util.Map;

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
            case "most-visited": handleMostVisited(eventPacket);
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
        controller.requestMostVisitedAnalytics(typeMap.get(eventPacket.selected()));
    }
}
