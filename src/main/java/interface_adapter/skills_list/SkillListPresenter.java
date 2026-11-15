package interface_adapter.skills_list;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;

/**
 * The Presenter for the Signup Use Case.
 */
public class SkillListPresenter implements SignupOutputBoundary {

    private final SkillListViewModel skillListViewModel;
    private final LoginViewModel loginViewModel;
    private final ViewManagerModel viewManagerModel;

    public SkillListPresenter(ViewManagerModel viewManagerModel,
                              SkillListViewModel skillListViewModel,
                              LoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.skillListViewModel = skillListViewModel;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void prepareSuccessView(SignupOutputData response) {
        // On success, switch to the login view.
        final LoginState loginState = loginViewModel.getState();
        loginState.setUsername(response.getUsername());
        loginViewModel.notifyStateChanged();

        viewManagerModel.setState(LoginViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final SkillListState skillListState = skillListViewModel.getState();
        skillListState.setUsernameError(error);
        skillListViewModel.notifyStateChanged();
    }

    @Override
    public void switchToLoginView() {
        viewManagerModel.setState(LoginViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }
}
