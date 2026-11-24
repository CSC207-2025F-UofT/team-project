package use_case.job_filter;

import entity.Student;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * The input data for the Job Filter Use Case.
 */
public class JobFilterInputData {

    private final List<String> skillsNeeded;
    private final String company;
    private final String startYear;
    private final String startMonth;
    private final String endYear;
    private final String endMonth;
    private final String endDay;
    private final String startDay;
    private final String location;
    private final String maxSalary;
    private final String minSalary;
    private final Student student;

    public JobFilterInputData(List<String> skillsNeeded,
                              String company,
                              String startYear,
                              String startMonth,
                              String startDay,
                              String endYear,
                              String endMonth,
                              String endDay,
                              String location,
                              String maxSalary,
                              String minSalary,
                              Student student) {
        this.skillsNeeded = skillsNeeded == null ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<>(skillsNeeded));
        this.company = company;
        this.startYear = startYear;
        this.startMonth = startMonth;
        this.startDay = startDay;
        this.endYear = endYear;
        this.endMonth = endMonth;
        this.endDay = endDay;
        this.location = location;
        this.maxSalary = maxSalary;
        this.minSalary = minSalary;
        this.student = Objects.requireNonNull(student, "student");
    }

    List<String> getSkillneeded() {
        return this.skillsNeeded;
    }

    String getCompany() {
        return this.company;
    }

    String get_start_Date_Y() {
        return this.startYear;
    }

    String get_start_Date_M() { return this.startMonth; }

    String get_start_Date_D() { return this.startDay; }

    String get_end_Date_Y() {
        return this.endYear;
    }

    String get_end_Date_M() { return this.endMonth; }

    String get_end_Date_D() { return this.endDay; }

    String getLocation() {
        return this.location;
    }

    String getmax_Salary() {
        return this.maxSalary;
    }

    String getmin_Salary() {
        return this.minSalary;
    }

    Student getStudent() {return this.student;}

}
