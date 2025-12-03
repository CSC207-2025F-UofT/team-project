package use_case.spending_report;

import java.util.Map;

import javax.swing.JFrame;

public class SpendingReportViewModel extends JFrame {
    private Map<String, Float> categoryData;
    private String currentMonth;
    private String chartType;

    public SpendingReportViewModel() {
        this.chartType = "Bar Chart";
    }

    public Map<String, Float> getCategoryData() { return categoryData; }

    public void setCategoryData(Map<String, Float> categoryData) {
        this.categoryData = categoryData;
    }

    public String getCurrentMonth() { return currentMonth; }
    public void setCurrentMonth(String month) { this.currentMonth = month; }
    
    public String getChartType() { return chartType; }
    public void setChartType(String chartType) { this.chartType = chartType; }
    
    public boolean hasData() {
        return categoryData != null && !categoryData.isEmpty();
    }

    // monthDropdown.addActionListener((ActionEvent e) -> {
    //     if (controller != null) {
    //         String selectedMonth = (String) monthDropdown.getSelectedItem();
    //         controller.generateReport(userId, selectedMonth);
    //     } else {
    //         System.err.println("Controller not set yet!");
    //     }
    // });

    // chartTypeDropdown.addActionListener((ActionEvent e) -> {
    //     if (controller != null) {
    //         String selectedMonth = (String) monthDropdown.getSelectedItem();
    //         controller.generateReport(userId, selectedMonth);
    //     }
    // });

}