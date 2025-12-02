package use_case.user_login;

public class FakeUserLoginPresenter implements UserLoginOutputBoundary {

    public String failMessage = null;
    public String successUsername = null;
    public String successUserType = null;
    public String goBackView = null;

    @Override
    public void prepareFailView(String error) {
        failMessage = error;
    }

    @Override
    public void prepareSuccessView(UserLoginOutputData data) {
        successUsername = data.getUsername();
        successUserType = data.getUserType();
    }

    @Override
    public void prepareGoBackView(String viewName) {
        goBackView = viewName;
    }
}
