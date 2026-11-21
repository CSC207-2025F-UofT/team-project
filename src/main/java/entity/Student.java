package entity;
import java.util.ArrayList;

/**
 * A simple entity representing a user. Users have a username and password..
 */
public class Student {

    private final String name;
    private final String password;
    private final ArrayList<String> courses;
    private final ArrayList<String> skills;
    private final ArrayList<JobGroup> jobGroups;

    /**
     * Creates a new user with the given non-empty name and non-empty password.
     * @param name the username
     * @param password the password
     * @throws IllegalArgumentException if the password or name are empty
     */
    public Student(String name, String password, ArrayList<String> courses, ArrayList<String> skills) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if ("".equals(password)) {
            throw new IllegalArgumentException("Password cannot be empty");
        }
        if (courses.isEmpty()) {
            throw new IllegalArgumentException("Courses cannot be empty");
        }
        if (skills.isEmpty()) {
            throw new IllegalArgumentException("Courses cannot be empty");
        }
        this.name = name;
        this.password = password;
        this.courses = new ArrayList<>(courses);
        this.skills = new ArrayList<>(skills);
        this.jobGroups = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<String> getCourses() { return new ArrayList<>(this.courses); }

    public ArrayList<String> getSkills() { return new ArrayList<>(this.skills); }

    public ArrayList<JobGroup> getJobGroups() {
        return new ArrayList<>(this.jobGroups);
    }

    public void addJobGroup(JobGroup jobGroup) {
        this.jobGroups.add(jobGroup);
    }

}
