package app;

import entity.Job;
import interface_adapter.job_list.JobListState;
import interface_adapter.job_list.JobListViewModel;
import view.JobListView;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Standalone preview for the JobList view with sample job listings.
 */
public final class JobListPreview {

    private JobListPreview() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final JobListViewModel viewModel = new JobListViewModel();
            final JobListState state = viewModel.getState();
            state.setJobs(createSampleJobs());
            viewModel.notifyStateChanged();

            final JFrame frame = new JFrame("Job List Preview");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setContentPane(new JobListView(viewModel));
            frame.setSize(1100, 700);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static ArrayList<Job> createSampleJobs() {
        final ArrayList<Job> jobs = new ArrayList<>();
        jobs.add(new Job(
                "Software Engineer Intern",
                new ArrayList<>(Arrays.asList("Java", "Git", "Data Structures")),
                "Northstar Technologies",
                "Posted recently",
                "Work with a product engineering team to build and test features. This internship includes mentorship, code reviews, and opportunities to contribute to production projects.",
                "Toronto, ON · Hybrid",
                "$28-$35 per hour",
                "https://example.com/jobs/software-engineer-intern"));
        jobs.add(new Job(
                "Junior Data Analyst",
                new ArrayList<>(Arrays.asList("SQL", "Python", "Data Visualization")),
                "Maple Leaf Analytics",
                "Posted recently",
                "Help analyze datasets, prepare reports, and communicate insights to cross-functional teams. Experience with SQL and Python is an asset.",
                "Toronto, ON · On-site",
                "$60,000-$75,000 per year",
                "https://example.com/jobs/junior-data-analyst"));
        jobs.add(new Job(
                "Frontend Developer Co-op",
                new ArrayList<>(Arrays.asList("JavaScript", "React", "CSS")),
                "Brightside Labs",
                "Posted recently",
                "Build accessible user interfaces and collaborate with designers and backend developers in an agile environment.",
                "Remote · Canada",
                "$25-$32 per hour",
                "https://example.com/jobs/frontend-developer-coop"));
        return jobs;
    }
}
