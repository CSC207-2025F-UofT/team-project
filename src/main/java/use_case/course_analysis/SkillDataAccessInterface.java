package use_case.course_analysis;

import entity.Student;

/**
 * The DAO interface for the Change Password Use Case.
 */
public interface SkillDataAccessInterface {

    /**
     * Updates the system to record this user's password.
     * @param student the user whose skills are to be updated
     */
    void changeSkills(Student student);

}
