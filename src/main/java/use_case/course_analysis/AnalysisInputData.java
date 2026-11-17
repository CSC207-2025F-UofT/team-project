package use_case.course_analysis;

import entity.Student;

import java.util.ArrayList;

/**
 * The input data for the Change Password Use Case.
 */
public class AnalysisInputData {

    private final Student student;

    public AnalysisInputData(Student student) {
        this.student = student;
    }

    String getName() {
        return this.student.getName();
    }

    String getPassword() {
        return this.student.getPassword();
    }

    ArrayList<String> getCourses() { return new ArrayList<>(this.student.getCourses()); }

}
