package user_case.Logout;


public class LogoutInteractor implements LogoutInputBoundary{
    private LogoutUserDataAccessInterface userDataAccessObject;
    private LogoutOutputBoundary logoutPresenter;

    public LogoutInteractor(LogoutUserDataAccessInterface logoutUserDataAccessInterface,
                            LogoutOutputBoundary logoutOutputBoundary){
        this.userDataAccessObject = logoutUserDataAccessInterface;
        this.logoutPresenter = logoutOutputBoundary;
    }
    @Override
    public void execute() {
        String username = userDataAccessObject.getUsername();

        userDataAccessObject.setCurrentUsername(null);

        LogoutOutputData outputData =
                new LogoutOutputData(username);

        logoutPresenter.PrepareSuccessView(outputData);
    }
}
