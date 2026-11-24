package interface_adapter.job_filter;

import entity.Student;
import java.util.List;
import use_case.job_filter.JobFilterInputBoundary;
import use_case.job_filter.JobFilterInputData;

public class JobFilterController {

    private final JobFilterInputBoundary jobFilterUseCaseInteractor;

    public JobFilterController(JobFilterInputBoundary jobFilterUseCaseInteractor) {
        this.jobFilterUseCaseInteractor = jobFilterUseCaseInteractor;
    }

    public void execute(List<String> skillsNeeded, String company,
                        String startYear, String startMonth, String startDay,
                        String endYear, String endMonth, String endDay,
                        String location, String maxSalary, String minSalary, Student student) {
        JobFilterInputData input = new JobFilterInputData(skillsNeeded, company,
                startYear, startMonth, startDay, endYear, endMonth, endDay,
                location, maxSalary, minSalary, student);
        jobFilterUseCaseInteractor.execute(input);
    }
}
