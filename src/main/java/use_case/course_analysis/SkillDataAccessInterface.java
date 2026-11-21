package use_case.course_analysis;

import entity.Student;

/**
 * The DAO interface for reading and updating a student's skills.
 */
public interface SkillDataAccessInterface {

    /**
     * Returns the student with the given username.
     * @param username the username to look up
     * @return the student, or null if there is no such student
     */
    Student get(String username);

    /**
     * Updates the system to record this student's skills.
     * @param student the student whose skills are to be updated
     */
    void changeSkills(Student student);

}
