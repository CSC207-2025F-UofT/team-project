package entity;

import java.util.ArrayList;

/**
 * Factory for creating student objects.
 */
public class StudentFactory {

    public Student create(String name, String password, ArrayList<String> courses, ArrayList<String> skills) {
        return new Student(name, password, courses, skills);
    }
}
