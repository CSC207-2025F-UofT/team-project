package use_case.enrol_course;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Output Data for the Enrol Course Use Case. Grades are given as plain labels
 * so that the presenter and view never need to import entities.
 */
public class EnrolCourseOutputData {

    private final String username;
    private final String courseCode;
    private final String courseName;
    private final String targetGrade;
    private final List<String> topicNames;

    public EnrolCourseOutputData(String username, String courseCode, String courseName,
                                 String targetGrade, List<String> topicNames) {
        this.username = username;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.targetGrade = targetGrade;
        this.topicNames = Collections.unmodifiableList(new ArrayList<>(topicNames));
    }

    public String getUsername() {
        return username;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getTargetGrade() {
        return targetGrade;
    }

    public List<String> getTopicNames() {
        return topicNames;
    }
}
