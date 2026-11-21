package interface_adapter.add_course;

import java.util.List;

import use_case.add_course.AddCourseInputBoundary;
import use_case.add_course.AddCourseInputData;

/**
 * Controller for the Add Course Use Case.
 */
public class AddCourseController {

    private final AddCourseInputBoundary addCourseInteractor;

    public AddCourseController(AddCourseInputBoundary addCourseInteractor) {
        this.addCourseInteractor = addCourseInteractor;
    }

    /**
     * Executes the Add Course Use Case.
     * @param rawCourseCodes the course codes the student entered
     */
    public void execute(List<String> rawCourseCodes) {
        addCourseInteractor.execute(new AddCourseInputData(rawCourseCodes));
    }

    /**
     * Executes the "switch to AddCourseView" Use Case.
     */
    public void switchToAddCourseView() {
        addCourseInteractor.switchToAddCourseView();
    }

    /**
     * Executes the "switch to LoggedInView" Use Case.
     */
    public void switchToLoggedInView() {
        addCourseInteractor.switchToLoggedInView();
    }
}
