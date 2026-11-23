package use_case.logout;

import entity.UserSession;

public class LogoutInteractor implements LogoutInputBoundary{
    private final LogoutOutputBoundary outputBoundary;
    private UserSession userSession;

    public LogoutInteractor(LogoutOutputBoundary outputBoundary, UserSession userSession){
        this.outputBoundary = outputBoundary;
        this.userSession = userSession;
    }

    @Override
    public void execute(){
        userSession.logout();
        outputBoundary.prepareSuccessView();
    }
}
