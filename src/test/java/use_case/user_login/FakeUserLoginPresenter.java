package use_case.user_login;

public class FakeUserLoginPresenter implements UserLoginOutputBoundary {

    public String failMessage;
    public UserLoginOutputData successData;
    public String goBackViewName;

    @Override
    public void prepareFailView(String error) {
        this.failMessage = error;
    }

    @Override
    public void prepareSuccessView(UserLoginOutputData data) {
        this.successData = data;
    }

    @Override
    public void prepareGoBackView(String viewName) {
        this.goBackViewName = viewName;
    }
}