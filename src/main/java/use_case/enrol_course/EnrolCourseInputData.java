package use_case.enrol_course;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Input Data for the Enrol Course Use Case.
 */
public class EnrolCourseInputData {

    private final String username;
    private final String courseCode;
    private final String courseName;
    private final String targetGrade;
    private final List<String> topicNames;

    /**
     * Creates the input data for enrolling in a course.
     * @param username the user who is enrolling
     * @param courseCode the course code typed by the user, such as "CSC207"
     * @param courseName the course name typed by the user
     * @param targetGrade the label of the target grade, such as "A-"
     * @param topicNames the main topics or chapters of the course
     */
    public EnrolCourseInputData(String username, String courseCode, String courseName,
                                String targetGrade, List<String> topicNames) {
        this.username = username;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.targetGrade = targetGrade;
        if (topicNames == null) {
            this.topicNames = Collections.emptyList();
        }
        else {
            this.topicNames = Collections.unmodifiableList(new ArrayList<>(topicNames));
        }
    }

    String getUsername() {
        return username;
    }

    String getCourseCode() {
        return courseCode;
    }

    String getCourseName() {
        return courseName;
    }

    String getTargetGrade() {
        return targetGrade;
    }

    List<String> getTopicNames() {
        return topicNames;
    }
}
