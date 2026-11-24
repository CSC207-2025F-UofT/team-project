package use_case.course_analysis;

import entity.Student;

/**
 * The DAO interface for the Change Password Use Case.
 */
public interface AnalysisDataAccessInterface {

    /**
     * Fetches a course description from a course code.
     * @param course to have its description fetched.
     */
    String getCourseDescription(String course);

    /**
     * Fetches skills from a description.
     * @param description to have its skills analyzed.
     */
    String getSkill(String description);
}
