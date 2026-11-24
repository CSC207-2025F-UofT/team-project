package use_case.job_filter;

import entity.Student;

/**
 * The DAO interface for the Job Filter Use Case.
 */
public interface JobFilterUserDataAccessInterface {

    /**
     * Requests job filtering for a student.
     * @param student the student whose jobs are being filtered
     */
    void filterJobs(Student student);
}
