package use_case.job_filter;

import entity.Job;
import entity.JobGroup;
import entity.Student;
import entity.StudentFactory;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.DateTimeException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The Change Password Interactor.
 */
public class JobFilterInteractor implements JobFilterInputBoundary {
    private final JobFilterUserDataAccessInterface userDataAccessObject;
    private final JobFilterOutputBoundary userPresenter;
    private final StudentFactory studentFactory;

    public JobFilterInteractor(JobFilterOutputBoundary presenter) {
        this(null, presenter, null);
    }

    public JobFilterInteractor(JobFilterUserDataAccessInterface jobFilterDataAccessInterface,
                               JobFilterOutputBoundary jobFilterOutputBoundary,
                               StudentFactory studentFactory) {
        this.userDataAccessObject = jobFilterDataAccessInterface;
        this.userPresenter = jobFilterOutputBoundary;
        this.studentFactory = studentFactory;
    }

    @Override
    public void execute(JobFilterInputData jobFilterInputData) {
        Student student = jobFilterInputData.getStudent();
        List<JobGroup> jobGroups = student.getJobGroups();
        List<Job> jobs = new ArrayList<>();

        for (JobGroup jobGroup : jobGroups) {
            for (Job job : jobGroup.getJobs()) {
                if (!jobs.contains(job)) {
                    jobs.add(job);
                }
            }
        }
        List<Job> result = new ArrayList<>();

        JobFilterInputData input = jobFilterInputData;
        try {
            LocalDate start = dateBound(input.get_start_Date_Y(), input.get_start_Date_M(), input.get_start_Date_D());
            LocalDate end = dateBound(input.get_end_Date_Y(), input.get_end_Date_M(), input.get_end_Date_D());
            double min = salaryBound(input.getmin_Salary(), Double.NEGATIVE_INFINITY);
            double max = salaryBound(input.getmax_Salary(), Double.POSITIVE_INFINITY);
            if (min > max || (start != null && end != null && start.isAfter(end))) {
                userPresenter.prepareFailView("The minimum must not exceed the maximum.");
                return;
            }
            for (Job job : jobs) {
                if ((blank(input.getCompany()) || input.getCompany().trim().equals(job.getCompany()))
                        && (blank(input.getLocation()) || input.getLocation().trim().equals(job.getLocations()))
                        && (input.getSkillneeded() == null || input.getSkillneeded().isEmpty()
                            || (job.getSkillsNeeded() != null && job.getSkillsNeeded().containsAll(input.getSkillneeded())))
                        && matchesDate(job.getDate(), start, end)
                        && matchesSalary(job.getSalary(), min, max)) {
                    result.add(job);
                }
            }
        } catch (DateTimeException | NumberFormatException ex) {
            userPresenter.prepareFailView("Enter a valid date and numeric salary bounds.");
            return;
        }
        userPresenter.prepareSuccessView(new JobFilterOutputData(result));
    }

    private static boolean blank(String value) {
        return value == null || value.isBlank();
    }

    private static LocalDate dateBound(String year, String month, String day) {
        if (blank(year) && blank(month) && blank(day)) {
            return null;
        }
        return LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
    }

    private static boolean matchesDate(String value, LocalDate start, LocalDate end) {
        if (start == null && end == null) {
            return true;
        }
        if (blank(value)) {
            return false;
        }
        try {
            LocalDate date = LocalDate.parse(value.trim());
            return (start == null || !date.isBefore(start)) && (end == null || !date.isAfter(end));
        } catch (DateTimeException ex) {
            return false;
        }
    }

    private static final String AMOUNT = "\\$?([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]+)?)";
    private static final Pattern SALARY = Pattern.compile(AMOUNT + "(?:\\s*-\\s*" + AMOUNT
            + ")?(?: per (?:hour|year))?", Pattern.CASE_INSENSITIVE);

    private static double salaryBound(String value, double fallback) {
        if (blank(value)) {
            return fallback;
        }
        double amount = Double.parseDouble(value.trim().replace(",", ""));
        if (!Double.isFinite(amount)) {
            throw new NumberFormatException();
        }
        return amount;
    }

    private static boolean matchesSalary(String value, double min, double max) {
        if (min == Double.NEGATIVE_INFINITY && max == Double.POSITIVE_INFINITY) {
            return true;
        }
        Matcher range = SALARY.matcher(value == null ? "" : value.trim());
        if (!range.matches()) {
            return false;
        }
        double low = salaryBound(range.group(1), 0);
        double high = salaryBound(range.group(2), low);
        return low <= high && high >= min && low <= max;
    }
}
