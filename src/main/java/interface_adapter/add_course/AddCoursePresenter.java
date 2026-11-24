package interface_adapter.add_course;

import java.util.ArrayList;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.add_course.AddCourseOutputBoundary;
import use_case.add_course.AddCourseOutputData;

/**
 * The Presenter for the Add Course Use Case.
 */
public class AddCoursePresenter implements AddCourseOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final AddCourseViewModel addCourseViewModel;

    public AddCoursePresenter(ViewManagerModel viewManagerModel, AddCourseViewModel addCourseViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.addCourseViewModel = addCourseViewModel;
    }

    @Override
    public void prepareSuccessView(AddCourseOutputData outputData) {
        final AddCourseState state = addCourseViewModel.getState();
        state.setCourses(new ArrayList<>(outputData.getAllCourses()));
        String message = "Added: " + String.join(", ", outputData.getAddedCourses());
        if (!outputData.getRejectedCourses().isEmpty()) {
            message += " | Not added: " + String.join(", ", outputData.getRejectedCourses());
        }
        state.setMessage(message);
        state.setError(null);
        addCourseViewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        final AddCourseState state = addCourseViewModel.getState();
        state.setMessage(null);
        state.setError(errorMessage);
        addCourseViewModel.notifyStateChanged();
    }

    @Override
    public void switchToAddCourseView() {
        viewManagerModel.setState(AddCourseViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

    @Override
    public void switchToLoggedInView() {
        viewManagerModel.setState(LoggedInViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }
}
