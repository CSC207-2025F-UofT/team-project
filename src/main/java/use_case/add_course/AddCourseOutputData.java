package use_case.add_course;

import java.util.List;

/**
 * The output data for the Add Course Use Case.
 */
public class AddCourseOutputData {

    private final List<String> addedCourses;
    private final List<String> rejectedCourses;
    private final List<String> allCourses;

    public AddCourseOutputData(List<String> addedCourses,
                               List<String> rejectedCourses,
                               List<String> allCourses) {
        this.addedCourses = addedCourses;
        this.rejectedCourses = rejectedCourses;
        this.allCourses = allCourses;
    }

    public List<String> getAddedCourses() {
        return addedCourses;
    }

    public List<String> getRejectedCourses() {
        return rejectedCourses;
    }

    public List<String> getAllCourses() {
        return allCourses;
    }
}