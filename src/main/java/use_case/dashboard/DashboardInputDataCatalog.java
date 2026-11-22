package use_case.dashboard;

import entity.LocationInfoType;

import java.util.Optional;

/**
 * Catalog for input data specific to the analytic types.
 */
public class DashboardInputDataCatalog {
    public record MostVisitedInputData(Optional<LocationInfoType> locationInfoType) implements DashboardInputData {
        @Override
        public AnalyticsType getAnalyticsType() {
            return AnalyticsType.MOST_VISITED;
        }
    }
}
