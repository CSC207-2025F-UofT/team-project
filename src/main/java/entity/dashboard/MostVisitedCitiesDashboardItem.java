package entity.dashboard;

import java.util.List;

/**
 * Analytics summary for most visited cities.
 */
public class MostVisitedCitiesDashboardItem extends DashboardItem {
    private List<DashboardDataCatalog.MostVisitedCity> mostVisitedCities;
    private int startYear;
    private int endYear;

    public MostVisitedCitiesDashboardItem(String title) {
        super(title);
        super.presentationType = PresentationType.RANKING_TABLE;
    }

    public List<DashboardDataCatalog.MostVisitedCity> getMostVisitedCities() {
        return mostVisitedCities;
    }
    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }
}
