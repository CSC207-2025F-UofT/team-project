package interface_adapter.enrol_course;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.enrol_course.EnrolCourseOutputBoundary;
import use_case.enrol_course.EnrolCourseOutputData;

/**
 * The Presenter for the Enrol Course Use Case.
 */
public class EnrolCoursePresenter implements EnrolCourseOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final EnrolViewModel enrolViewModel;

    public EnrolCoursePresenter(ViewManagerModel viewManagerModel, EnrolViewModel enrolViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.enrolViewModel = enrolViewModel;
    }

    @Override
    public void prepareSuccessView(EnrolCourseOutputData outputData) {
        final EnrolState state = enrolViewModel.getState();
        state.setErrorMessage(null);
        state.setEnrolledCourseCode(outputData.getCourseCode());
        enrolViewModel.notifyStateChanged();

        // The View Enrolled Courses use case refreshes the course list on the home view.
        switchToHomeView();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final EnrolState state = enrolViewModel.getState();
        state.setEnrolledCourseCode(null);
        state.setErrorMessage(errorMessage);
        enrolViewModel.notifyStateChanged();
    }

    @Override
    public void switchToEnrolView() {
        enrolViewModel.setState(new EnrolState());
        viewManagerModel.setState(EnrolViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

    @Override
    public void switchToHomeView() {
        // TODO: use HomeViewModel.VIEW_NAME once HomeView replaces LoggedInView.
        viewManagerModel.setState(LoggedInViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }
}
