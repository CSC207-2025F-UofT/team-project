package view;

import entity.Job;
import interface_adapter.job_list.JobListController;
import interface_adapter.job_list.JobListState;
import interface_adapter.job_list.JobListViewModel;
import interface_adapter.job_list.JobListViewModelListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Displays matching jobs in a selectable list with details for the selected job.
 */
public class JobListView extends JPanel implements JobListViewModelListener {

    private static final Color LINKEDIN_BLUE = new Color(10, 102, 194);
    private static final Color TEXT_COLOR = new Color(35, 40, 45);
    private static final Color MUTED_COLOR = new Color(95, 105, 115);
    private final JobListViewModel jobListViewModel;
    private final DefaultListModel<Job> jobsListModel = new DefaultListModel<>();
    private final JList<Job> jobsList = new JList<>(jobsListModel);

    private final JLabel errorLabel = new JLabel(" ");
    private final JLabel detailTitle = new JLabel("Select a job to see details");
    private final JLabel detailCompany = new JLabel(" ");
    private final JLabel detailLocation = new JLabel(" ");
    private final JLabel detailDate = new JLabel(" ");
    private final JLabel detailSalary = new JLabel(" ");
    private final JLabel detailSkills = new JLabel(" ");
    private final JTextArea detailDescription = new JTextArea();
    private final JLabel detailUrl = new JLabel(" ");
    private String currentJobUrl;

    private boolean updatingJobs;
    private JobListController jobListController;

    public JobListView(JobListViewModel jobListViewModel) {
        this.jobListViewModel = jobListViewModel;
        this.jobListViewModel.addListener(this);

        setLayout(new BorderLayout(0, 12));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        final JLabel pageTitle = new JLabel(JobListViewModel.TITLE_LABEL);
        pageTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        pageTitle.setForeground(TEXT_COLOR);
        add(pageTitle, BorderLayout.NORTH);

        final JButton backButton = new JButton("Back");
        backButton.addActionListener(event -> {
            if (jobListController != null) {
                jobListController.switchToLoggedInView();
            }
        });
        final JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(Color.WHITE);
        footer.add(backButton, BorderLayout.WEST);
        add(footer, BorderLayout.SOUTH);

        final JSplitPane jobSplitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, createJobsPanel(), createDetailsPanel());
        jobSplitPane.setResizeWeight(0.38);
        jobSplitPane.setDividerLocation(360);
        jobSplitPane.setBorder(null);
        jobSplitPane.setContinuousLayout(true);
        add(jobSplitPane, BorderLayout.CENTER);

        stateChanged();
    }

    private JPanel createJobsPanel() {
        final JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Color.WHITE);

        final JLabel listTitle = new JLabel("Recommended jobs");
        listTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        listTitle.setForeground(TEXT_COLOR);

        errorLabel.setForeground(new Color(180, 35, 35));
        errorLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        final JPanel heading = new JPanel(new BorderLayout());
        heading.setBackground(Color.WHITE);
        heading.add(listTitle, BorderLayout.NORTH);
        heading.add(errorLabel, BorderLayout.SOUTH);
        panel.add(heading, BorderLayout.NORTH);

        jobsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jobsList.setBackground(Color.WHITE);
        jobsList.setCellRenderer(new JobListCellRenderer());
        jobsList.addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && !updatingJobs) {
                showJobDetails(jobsList.getSelectedValue());
            }
        });

        final JScrollPane jobsScrollPane = new JScrollPane(jobsList);
        jobsScrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)));
        panel.add(jobsScrollPane, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(340, 500));
        return panel;
    }

    private JScrollPane createDetailsPanel() {
        final JPanel details = new JPanel();
        details.setLayout(new javax.swing.BoxLayout(details, javax.swing.BoxLayout.Y_AXIS));
        details.setBackground(Color.WHITE);
        details.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        detailTitle.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 23));
        detailTitle.setForeground(TEXT_COLOR);
        detailTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        detailCompany.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        detailCompany.setForeground(TEXT_COLOR);
        detailCompany.setAlignmentX(Component.LEFT_ALIGNMENT);

        detailLocation.setForeground(MUTED_COLOR);
        detailLocation.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailDate.setForeground(MUTED_COLOR);
        detailDate.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailSalary.setForeground(MUTED_COLOR);
        detailSalary.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailSkills.setForeground(TEXT_COLOR);
        detailSkills.setAlignmentX(Component.LEFT_ALIGNMENT);

        final JLabel descriptionHeading = sectionHeading("About the job");
        detailDescription.setEditable(false);
        detailDescription.setLineWrap(true);
        detailDescription.setWrapStyleWord(true);
        detailDescription.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        detailDescription.setForeground(TEXT_COLOR);
        detailDescription.setBackground(Color.WHITE);
        detailDescription.setColumns(40);
        detailDescription.setRows(7);
        detailDescription.setMargin(new Insets(0, 0, 0, 0));
        detailDescription.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailDescription.setFocusable(false);

        final JLabel linkHeading = sectionHeading("Job link");
        detailUrl.setForeground(LINKEDIN_BLUE);
        detailUrl.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailUrl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                openCurrentJobUrl();
            }
        });

        details.add(detailTitle);
        details.add(javax.swing.Box.createVerticalStrut(8));
        details.add(detailCompany);
        details.add(javax.swing.Box.createVerticalStrut(12));
        details.add(detailLocation);
        details.add(javax.swing.Box.createVerticalStrut(5));
        details.add(detailDate);
        details.add(javax.swing.Box.createVerticalStrut(5));
        details.add(detailSalary);
        details.add(javax.swing.Box.createVerticalStrut(14));
        details.add(detailSkills);
        details.add(javax.swing.Box.createVerticalStrut(20));
        details.add(descriptionHeading);
        details.add(javax.swing.Box.createVerticalStrut(8));
        details.add(detailDescription);
        details.add(javax.swing.Box.createVerticalStrut(20));
        details.add(linkHeading);
        details.add(javax.swing.Box.createVerticalStrut(8));
        details.add(detailUrl);

        final JScrollPane scrollPane = new JScrollPane(details);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 225, 230)));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }

    private JLabel sectionHeading(String text) {
        final JLabel heading = new JLabel(text);
        heading.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        heading.setForeground(TEXT_COLOR);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);
        return heading;
    }

    @Override
    public void stateChanged() {
        final JobListState state = jobListViewModel.getState();
        final Job selectedJob = jobsList.getSelectedValue();
        final int selectedJobId = selectedJob == null ? -1 : selectedJob.getId();
        final ArrayList<Job> jobs = state.getJobs();

        updatingJobs = true;
        jobsListModel.clear();
        for (Job job : jobs) {
            jobsListModel.addElement(job);
        }

        int selectedIndex = -1;
        for (int i = 0; i < jobsListModel.size(); i++) {
            if (jobsListModel.get(i).getId() == selectedJobId) {
                selectedIndex = i;
                break;
            }
        }
        if (selectedIndex < 0 && !jobsListModel.isEmpty()) {
            selectedIndex = 0;
        }
        jobsList.setSelectedIndex(selectedIndex);
        updatingJobs = false;

        final String error = state.getError();
        errorLabel.setText(error == null || error.isBlank() ? " " : error);
        showJobDetails(jobsList.getSelectedValue());
    }

    public void setJobListController(JobListController jobListController) {
        this.jobListController = jobListController;
    }

    private void showJobDetails(Job job) {
        if (job == null) {
            detailTitle.setText("Select a job to see details");
            detailCompany.setText(" ");
            detailLocation.setText(" ");
            detailDate.setText(" ");
            detailSalary.setText(" ");
            detailSkills.setText(" ");
            detailDescription.setText("");
            setJobLink(null);
            return;
        }

        detailTitle.setText(displayValue(job.getTitle()));
        detailCompany.setText(displayValue(job.getCompany()));
        detailLocation.setText("Location: " + displayValue(job.getLocations()));
        detailDate.setText("Posted: " + displayValue(job.getDate()));
        detailSalary.setText("Salary: " + displayValue(job.getSalary()));
        detailSkills.setText("Skills: " + formatSkills(job.getSkillsNeeded()));
        detailDescription.setText(displayValue(job.getDescription()));
        detailDescription.setCaretPosition(0);
        setJobLink(job.getUrl());
    }

    /**
     * Shows the job link as underlined, clickable text, or "Not specified" if there is no link.
     */
    private void setJobLink(String url) {
        if (url == null || url.isBlank()) {
            currentJobUrl = null;
            detailUrl.setText(url == null ? " " : "Not specified");
            detailUrl.setForeground(MUTED_COLOR);
            detailUrl.setCursor(Cursor.getDefaultCursor());
            detailUrl.setToolTipText(null);
            return;
        }
        currentJobUrl = url;
        detailUrl.setText("<html><u>View job posting</u></html>");
        detailUrl.setForeground(LINKEDIN_BLUE);
        detailUrl.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        detailUrl.setToolTipText(url);
    }

    /**
     * Opens the selected job's link in the default web browser.
     */
    private void openCurrentJobUrl() {
        if (currentJobUrl == null) {
            return;
        }
        try {
            final URI uri = new URI(currentJobUrl);
            final String scheme = uri.getScheme();
            if (!"http".equalsIgnoreCase(scheme) && !"https".equalsIgnoreCase(scheme)) {
                throw new URISyntaxException(currentJobUrl, "Not a web link");
            }
            if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                throw new UnsupportedOperationException("Opening a browser is not supported");
            }
            Desktop.getDesktop().browse(uri);
        }
        catch (IOException | URISyntaxException | UnsupportedOperationException exception) {
            JOptionPane.showMessageDialog(this,
                    "Could not open the link in your browser. You can copy it from here:\n" + currentJobUrl,
                    "Job link", JOptionPane.WARNING_MESSAGE);
        }
    }

    private String formatSkills(ArrayList<String> skills) {
        if (skills == null || skills.isEmpty()) {
            return "Not specified";
        }
        return String.join(", ", skills);
    }

    private static String displayValue(String value) {
        return value == null || value.isBlank() ? "Not specified" : value;
    }

    private static class JobListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                     boolean isSelected, boolean cellHasFocus) {
            final Job job = (Job) value;
            final JPanel card = new JPanel(new BorderLayout(0, 5));
            card.setOpaque(true);
            card.setBackground(isSelected ? new Color(232, 242, 250) : Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(225, 229, 233)),
                    BorderFactory.createEmptyBorder(12, 14, 12, 14)));

            final JLabel title = new JLabel(displayValue(job.getTitle()));
            title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
            title.setForeground(LINKEDIN_BLUE);

            final JLabel company = new JLabel(displayValue(job.getCompany()));
            company.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
            company.setForeground(TEXT_COLOR);

            final JLabel location = new JLabel(displayValue(job.getLocations()));
            location.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
            location.setForeground(MUTED_COLOR);

            final JPanel text = new JPanel(new BorderLayout(0, 4));
            text.setOpaque(false);
            text.add(title, BorderLayout.NORTH);

            final JPanel secondary = new JPanel(new BorderLayout(0, 3));
            secondary.setOpaque(false);
            secondary.add(company, BorderLayout.NORTH);
            secondary.add(location, BorderLayout.SOUTH);
            text.add(secondary, BorderLayout.CENTER);
            card.add(text, BorderLayout.CENTER);

            return card;
        }
    }
}
