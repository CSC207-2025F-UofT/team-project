package use_case.spending_report;

import java.util.Map;

import interface_adapter.ViewModel;

public class SpendingReportViewModel extends ViewModel<SpendingReportState> {

    private Map<String, Float> categoryData;
    private String currentMonth;
    private String chartType;
    private final String viewName = "spending report";

    public SpendingReportViewModel() {
        super("spending report");
        setState(new SpendingReportState());
        this.chartType = "Bar Chart";
    }

    public void setUsername(String username) {
        this.getState().setUsername(username);
    }

    public Map<String, Float> getCategoryData() {
        return categoryData;
    }

    public void setCategoryData(Map<String, Float> categoryData) {
        this.categoryData = categoryData;
    }

    public String getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(String month) {
        this.currentMonth = month;
    }

    public String getChartType() {
        return chartType;
    }

    public void setChartType(String chartType) {
        this.chartType = chartType;
    }

    @Override
    public String getViewName() {
        return viewName;
    }

    public boolean hasData() {
        return categoryData != null && !categoryData.isEmpty();
    }

}
