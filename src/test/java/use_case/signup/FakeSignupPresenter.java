package use_case.signup;

public class FakeSignupPresenter implements SignupOutputBoundary {

    public String failMessage = null;
    public String successUsername = null;
    public boolean loginViewSwitched = false;
    public String goBackView = null;

    @Override
    public void prepareFailView(String error) {
        failMessage = error;
    }

    @Override
    public void prepareSuccessView(SignupOutputData data) {
        successUsername = data.getUsername();
    }

    @Override
    public void switchToLoginView() {
        loginViewSwitched = true;
    }

    @Override
    public void prepareGoBackView(String viewName) {
        goBackView = viewName;
    }
}
