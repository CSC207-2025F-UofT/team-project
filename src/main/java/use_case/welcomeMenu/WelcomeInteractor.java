package use_case.welcomeMenu;

public class WelcomeInteractor implements WelcomeInputBoundary {
    //private final WelcomeDataAccessInterface welcomeDataAccessObject;
    private final WelcomeOutputBoundary welcomePresenter;

    public WelcomeInteractor(//WelcomeDataAccessInterface userDataAccessInterface,
                           WelcomeOutputBoundary welcomeOutputBoundary) {
        //this.userDataAccessObject = userDataAccessInterface;
        this.welcomePresenter = welcomeOutputBoundary;
    }

    //TODO this is going to have to start a new game
//    @Override
//    public void execute(WelcomeInputData welcomeInputData) {
//    }
}
