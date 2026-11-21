package use_case.add_course;

/**
 * The output boundary for the Add Course Use Case.
 */
public interface AddCourseOutputBoundary {

    /**
     * Prepares the success view for the Add Course Use Case.
     * @param addCourseOutputData the courses that were added and rejected
     */
    void prepareSuccessView(AddCourseOutputData addCourseOutputData);

    /**
     * Prepares the failure view for the Add Course Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}