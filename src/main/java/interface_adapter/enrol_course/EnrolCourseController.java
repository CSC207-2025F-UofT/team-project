package interface_adapter.enrol_course;

import interface_adapter.logged_in.LoggedInViewModel;
import use_case.enrol_course.EnrolCourseInputBoundary;
import use_case.enrol_course.EnrolCourseInputData;

import java.util.Arrays;
import java.util.List;

/**
 * Controller for the Enrol Course Use Case.
 */
public class EnrolCourseController {

    private final EnrolCourseInputBoundary enrolCourseInteractor;
    private final LoggedInViewModel homeViewModel;

    /**
     * Creates the controller.
     * @param enrolCourseInteractor the use case to run
     * @param homeViewModel the home view model, which holds the logged-in username
     */
    public EnrolCourseController(EnrolCourseInputBoundary enrolCourseInteractor,
                                 LoggedInViewModel homeViewModel) {
        this.enrolCourseInteractor = enrolCourseInteractor;
        this.homeViewModel = homeViewModel;
    }

    /**
     * Executes the Enrol Course Use Case for the logged-in user.
     * @param courseCode the course code
     * @param courseName the course name
     * @param targetGrade the target grade label, such as "A-"
     * @param topicsText the topics typed by the user, one per line or separated by commas
     */
    public void execute(String courseCode, String courseName, String targetGrade, String topicsText) {
        final String username = homeViewModel.getState().getUsername();
        final EnrolCourseInputData inputData = new EnrolCourseInputData(
                username, courseCode, courseName, targetGrade, splitTopics(topicsText));
        enrolCourseInteractor.execute(inputData);
    }

    /**
     * Opens the enrol form.
     */
    public void switchToEnrolView() {
        enrolCourseInteractor.switchToEnrolView();
    }

    /**
     * Closes the enrol form and goes back to the home view.
     */
    public void switchToHomeView() {
        enrolCourseInteractor.switchToHomeView();
    }

    private static List<String> splitTopics(String topicsText) {
        if (topicsText == null) {
            return Arrays.asList();
        }
        return Arrays.asList(topicsText.split("[,\\n]"));
    }
}
