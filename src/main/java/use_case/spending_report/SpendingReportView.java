package use_case.spending_report;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class SpendingReportView extends JFrame {
    private final JComboBox<String> monthDropdown;
    private final JComboBox<String> chartTypeDropdown;
    private final JPanel chartPanelContainer;
    
    public SpendingReportView() {
        setTitle("Monthly Spending Report");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        monthDropdown = new JComboBox<>(new String[]{
            "January 2025", "February 2025", "March 2025", "April 2025",
            "May 2025", "June 2025", "July 2025", "August 2025",
            "September 2025", "October 2025", "November 2025"
        });
        
        chartTypeDropdown = new JComboBox<>(new String[]{
            "Bar Chart", "Pie Chart"
        });

        topPanel.add(new JLabel("Select Month:"));
        topPanel.add(monthDropdown);
        topPanel.add(new JLabel("Chart Type:"));
        topPanel.add(chartTypeDropdown);
        add(topPanel, BorderLayout.NORTH);

        chartPanelContainer = new JPanel(new BorderLayout());
        add(chartPanelContainer, BorderLayout.CENTER);
    }
    
    public JComboBox<String> getMonthDropdown() { return monthDropdown; }
    public JComboBox<String> getChartTypeDropdown() { return chartTypeDropdown; }
    public JPanel getChartPanelContainer() { return chartPanelContainer; }
    
    public void addMonthDropdownListener(ActionListener listener) {
        monthDropdown.addActionListener(listener);
    }
    
    public void addChartTypeDropdownListener(ActionListener listener) {
        chartTypeDropdown.addActionListener(listener);
    }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
    
    public void displayChart(Map<String, Float> categoryData, String month) {
        if (categoryData == null || categoryData.isEmpty()) {
            chartPanelContainer.removeAll();
            chartPanelContainer.revalidate();
            chartPanelContainer.repaint();
            JOptionPane.showMessageDialog(this, "No transactions found for this month.");
            return;
        }
        chartPanelContainer.removeAll();
        String type = (String) chartTypeDropdown.getSelectedItem();
        
        JPanel chartPanel = ChartVisualizer.createChart(categoryData, month, type);
        chartPanelContainer.add(chartPanel, BorderLayout.CENTER);

        chartPanelContainer.revalidate();
        chartPanelContainer.repaint();
    }

    public void setInitialMonth(String month) {
        monthDropdown.setSelectedItem(month);
    }
}
