package view;

import entity.Job;
import entity.Student;
import interface_adapter.job_filter.JobFilterController;
import interface_adapter.job_filter.JobFilterState;
import interface_adapter.job_filter.JobFilterViewModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class JobFilterView extends JPanel {
    private final DefaultTableModel results = new DefaultTableModel(
            new String[]{"Job", "Company", "Location", "Date", "Salary"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JLabel status = new JLabel(" ");

    public JobFilterView(JobFilterController controller, JobFilterViewModel model, Student student) {
        super(new BorderLayout(16, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JPanel criteria = new JPanel();
        criteria.setLayout(new BoxLayout(criteria, BoxLayout.Y_AXIS));
        DateFields start = new DateFields("Application start");
        DateFields end = new DateFields("Application end");
        criteria.add(start);
        criteria.add(end);
        JPanel fields = new JPanel(new GridLayout(0, 2, 6, 6));
        JTextField location = addField(fields, "Location", 12);
        JTextField minimum = addField(fields, "Minimum salary", 8);
        JTextField maximum = addField(fields, "Maximum salary", 8);
        criteria.add(fields);
        JPanel left = new JPanel(new BorderLayout());
        left.add(criteria, BorderLayout.NORTH);
        add(left, BorderLayout.WEST);

        JPanel right = new JPanel(new BorderLayout());
        right.setBorder(BorderFactory.createTitledBorder("Matching jobs"));
        right.add(new JScrollPane(new JTable(results)), BorderLayout.CENTER);
        right.add(status, BorderLayout.SOUTH);
        add(right, BorderLayout.CENTER);

        JButton filter = new JButton("Filter");
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER));
        actions.add(filter);
        add(actions, BorderLayout.SOUTH);
        model.addListener(() -> showResults(model.getState()));
        filter.addActionListener(event -> controller.execute(List.of(), "",
                start.year.getText(), start.month.getText(), start.day.getText(),
                end.year.getText(), end.month.getText(), end.day.getText(),
                location.getText(), maximum.getText(), minimum.getText(), student));
    }

    private void showResults(JobFilterState state) {
        results.setRowCount(0);
        for (Job job : state.getJobs()) {
            results.addRow(new Object[]{job.getTitle(), job.getCompany(), job.getLocations(),
                    job.getDate(), job.getSalary()});
        }
        status.setText(state.getErrorMessage() == null
                ? "Matching jobs: " + results.getRowCount() : state.getErrorMessage());
    }

    private static JTextField addField(JPanel panel, String name, int columns) {
        JLabel label = new JLabel(name);
        JTextField field = new JTextField(columns);
        label.setLabelFor(field);
        panel.add(label);
        panel.add(field);
        return field;
    }

    private static class DateFields extends JPanel {
        private final JTextField day;
        private final JTextField month;
        private final JTextField year;

        DateFields(String title) {
            super(new FlowLayout(FlowLayout.LEFT));
            setBorder(BorderFactory.createTitledBorder(title));
            day = addField(this, "Day", 2);
            month = addField(this, "Month", 2);
            year = addField(this, "Year", 4);
        }
    }
}
