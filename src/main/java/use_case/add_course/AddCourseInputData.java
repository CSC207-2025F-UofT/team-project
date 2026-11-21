package use_case.add_course;

import java.util.List;

/**
 * The input data for the Add Course Use Case.
 */
public class AddCourseInputData {

    private final List<String> rawCourseCodes;

    public AddCourseInputData(List<String> rawCourseCodes) {
        this.rawCourseCodes = rawCourseCodes;
    }

    public List<String> getRawCourseCodes() {
        return rawCourseCodes;
    }
}

