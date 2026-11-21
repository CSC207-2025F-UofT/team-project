package use_case.job_match;

import entity.Student;

/**
 * The input data for the Job Match Use Case.
 */
public class JobMatchInputData {

    private final Student student;

    public JobMatchInputData(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }

}
