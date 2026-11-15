package use_case.login;

import entity.Student;

/**
 * DAO interface for the Login Use Case.
 */
public interface LoginUserDataAccessInterface {

    /**
     * Checks if the given username exists.
     * @param username the username to look for
     * @return true if a user with the given username exists; false otherwise
     */
    boolean existsByName(String username);

    /**
     * Saves the user.
     * @param student the user to save
     */
    void save(Student student);

    /**
     * Returns the user with the given username.
     * @param username the username to look up
     * @return the user with the given username
     */
    Student get(String username);

    void setCurrentUsername(String name);

    String getCurrentUsername();
}
