package entity;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String password;
    private USER_TYPE userType;
    private List<Course> courses;

    public enum USER_TYPE {
        STUDENT,
        INSTRUCTOR
    }

    public User(String name, String password, USER_TYPE userType) {
        this(name, password, userType, new ArrayList<>());
    }

    public User(String name, String password, USER_TYPE userType, List<Course> courses) {
        this.name = name;
        this.password = password;
        this.userType =  userType;
        this.courses = new ArrayList<>(courses);
    }

    public String getName() {
        return this.name;
    }

    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }

    public Course getCourse(String course) {
        for (Course c : this.courses) {
            if (c.getCourseCode().equals(course)) {
                return c;
            }
        }
        return null;
    }

    public void addCourse(Course course) {
        this.courses.add(course);
    }
}
