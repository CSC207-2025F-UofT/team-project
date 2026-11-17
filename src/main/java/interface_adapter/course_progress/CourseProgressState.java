package interface_adapter.course_progress;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The state for the Course Progress View Model: the course currently open on the progress page.
 * All values are plain strings, ready to show.
 */
public class CourseProgressState {
    private String username = "";
    private String courseCode = "";
    private String courseName = "";
    private String estimatedGrade = "";
    private String targetGrade = "";
    private String targetMessage = "";
    private String targetGradeError;
    private List<AssessmentRow> assessments = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getEstimatedGrade() {
        return estimatedGrade;
    }

    public void setEstimatedGrade(String estimatedGrade) {
        this.estimatedGrade = estimatedGrade;
    }

    public String getTargetGrade() {
        return targetGrade;
    }

    public void setTargetGrade(String targetGrade) {
        this.targetGrade = targetGrade;
    }

    /**
     * Returns the sentence telling the student what they need on the rest of the course.
     * @return the target message, or "" if there is none
     */
    public String getTargetMessage() {
        return targetMessage;
    }

    public void setTargetMessage(String targetMessage) {
        this.targetMessage = targetMessage;
    }

    /**
     * Returns why the last attempt to set the target grade failed.
     * @return the error, or null if the last attempt succeeded
     */
    public String getTargetGradeError() {
        return targetGradeError;
    }

    public void setTargetGradeError(String targetGradeError) {
        this.targetGradeError = targetGradeError;
    }

    /**
     * Returns the saved assessments of the open course. The grade calculator starts from these,
     * so whoever opens a course (View Course Progress) should fill them in.
     * @return a read-only list of the saved assessments
     */
    public List<AssessmentRow> getAssessments() {
        return Collections.unmodifiableList(assessments);
    }

    public void setAssessments(List<AssessmentRow> assessments) {
        this.assessments = new ArrayList<>(assessments);
    }
}
