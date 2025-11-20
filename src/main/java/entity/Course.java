package entity;

import java.util.List;

public class Course {
    private String courseName;
    private String courseCode;
    private List<User> instructors;
    private List<User> students;
    private List<Assignment> assignments;

    public Course(String courseName, String courseCode) {
        this.courseName = courseName;
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public List<User> getInstructors() {
        return instructors;
    }

    public List<User> getStudents() {
        return students;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }
}
