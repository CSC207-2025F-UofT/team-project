package entity;

/** Creates student users with sequential unique user IDs. */
public class StudentUserFactory extends UserFactory {

    /** Creates a factory whose first user ID is 1. */
    public StudentUserFactory() {
        super();
    }

    /** Creates a factory using the next available user ID restored from storage. */
    public StudentUserFactory(int nextUniqueId) {
        super(nextUniqueId);
    }

    /**
     * Creates a student user with the selected country and an empty task list.
     * The user's task factory starts at task ID 1.
     */
    public StudentTaskUser createStudentUser(
            String username,
            String password,
            Country countryCode) {

        // Validate before allocating a user ID.
        if (countryCode == null) {
            throw new IllegalArgumentException("Country code cannot be null.");
        }

        StudentTaskUser user = new StudentTaskUser(
                username, password, generateNextUniqueId());
        user.setCountryCode(countryCode);

        return user;
    }
}