package use_case.add_course;

/**
 * The input boundary for the Add Course Use Case.
 */
public interface AddCourseInputBoundary {

    /**
     * Executes the Add Course Use Case.
     * @param addCourseInputData the course codes the student entered
     */
    void execute(AddCourseInputData addCourseInputData);

    /**
     * Executes the "switch to AddCourseView" Use Case.
     */
    void switchToAddCourseView();

    /**
     * Executes the "switch to LoggedInView" Use Case.
     */
    void switchToLoggedInView();

}