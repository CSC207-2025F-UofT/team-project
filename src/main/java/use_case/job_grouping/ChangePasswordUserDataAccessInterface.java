package use_case.job_grouping;

import entity.Student;

/**
 * The DAO interface for the Change Password Use Case.
 */
public interface ChangePasswordUserDataAccessInterface {

    /**
     * Updates the system to record this user's password.
     * @param student the user whose password is to be updated
     */
    void changePassword(Student student);
}
