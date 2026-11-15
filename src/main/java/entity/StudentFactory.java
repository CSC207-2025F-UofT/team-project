package entity;

/**
 * Factory for creating CommonUser objects.
 */
public class StudentFactory {

    public Student create(String name, String password) {
        return new Student(name, password);
    }
}
