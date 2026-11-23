package use_case.login;

import entity.User;
import entity.UserSession;
import use_case.user.PasswordHasher;
import use_case.user.UserDataAccessInterface;

public class LoginInteractor implements LoginInputBoundary{
    private final UserDataAccessInterface userDataAccess;
    private final LoginOutputBoundary outputBoundary;
    private final PasswordHasher passwordHasher;
    private final UserSession userSession;

    public LoginInteractor(UserDataAccessInterface userDataAccess, LoginOutputBoundary outputBoundary, PasswordHasher passwordHasher, UserSession userSession){
        this.userDataAccess = userDataAccess;
        this.outputBoundary = outputBoundary;
        this.passwordHasher = passwordHasher;
        this.userSession = userSession;
    }

    @Override
    public void execute(LoginInputData inputData){
        String username = inputData.getUsername();
        String password = inputData.getPassword();

        if (username == null || !userDataAccess.existsByName(username)){
            outputBoundary.prepareFailView("Invalid username or password");
            return;
        }

        User user = userDataAccess.get(username);

        if (user == null || password == null || !passwordHasher.verify(password, user.getPasswordHash())){
            outputBoundary.prepareFailView("Invalid username or password");
            return;
        }

        userSession.login(username);
        LoginOutputData outputData = new LoginOutputData(username);
        outputBoundary.prepareSuccessView(outputData);
    }
}
