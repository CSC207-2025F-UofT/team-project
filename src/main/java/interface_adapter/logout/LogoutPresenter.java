package interface_adapter.logout;

import interface_adapter.ViewManagerModel;
import use_case.logout.LogoutOutputBoundary;
import view.LoginView;

/**
 * Presenter for the Logout use case.
 */
public class LogoutPresenter implements LogoutOutputBoundary {

    private final ViewManagerModel viewManagerModel;
    private final String loginViewName;
    private final Runnable clearLoginFields;

    public LogoutPresenter(ViewManagerModel viewManagerModel,
                           String loginViewName, Runnable clearLoginFields) {
        this.viewManagerModel = viewManagerModel;
        this.loginViewName = loginViewName;
        this.clearLoginFields = clearLoginFields;
    }

    @Override
    public void prepareSuccessView() {
        clearLoginFields.run();

        viewManagerModel.setState(loginViewName);
        viewManagerModel.firePropertyChanged();
    }
}
