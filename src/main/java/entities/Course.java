package entities;
import java.util.*;

public class Course {
    private final String name;
    private final int year;
    private List<Course> prerequisites;
    private List<Course> corequisites;

    public Course(String name, int year) {
        this.name = name;
        this.year = year;
    }

    public void addPrerequisite(Course prereq) {
        this.prerequisites.add(prereq);
    }

    public void addCorequisite(Course coreq) {
        this.corequisites.add(coreq);
    }

    public int getYear() {
        return year;
    }

    public String getName() {
        return name;
    }

    public List<Course> getPrerequisites() {
        return prerequisites;
    }

    public List<Course> getCorequisites() {
        return corequisites;
    }
}
