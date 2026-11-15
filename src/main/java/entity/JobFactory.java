package entity;

/**
 * Factory for creating CommonUser objects.
 */
public class JobFactory {

    public Job create(String name, String password) {
        return new Job(name, password);
    }
}
