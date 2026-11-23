package use_case.signup;

import entity.User;
import use_case.user.PasswordHasher;
import use_case.user.UserDataAccessInterface;

public class SignupInteractor implements SignupInputBoundary{
    private final UserDataAccessInterface userDataAccess;
    private final SignupOutputBoundary outputBoundary;
    private final PasswordHasher passwordHasher;

    public SignupInteractor(UserDataAccessInterface userDataAccess, SignupOutputBoundary outputBoundary, PasswordHasher passwordHasher){
        this.userDataAccess = userDataAccess;
        this.outputBoundary = outputBoundary;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public void execute(SignupInputData inputData){
        String username = inputData.getUsername();
        String password = inputData.getPassword();
        String repeatPassword = inputData.getRepeatPassword();

        if (username == null || username.trim().isEmpty()){
            outputBoundary.prepareFailView("Username cannot be empty");
            return;
        }
        if (password == null || password.isEmpty()){
            outputBoundary.prepareFailView("Password cannot be empty");
            return;
        }
        if (!password.equals(repeatPassword)){
            outputBoundary.prepareFailView("Passwords do not match");
            return;
        }
        if (userDataAccess.existsByName(username)){
            outputBoundary.prepareFailView("Username already exists");
            return;
        }
        String hashedPassword = passwordHasher.hash(password);
        User user = new User(username, hashedPassword);
        userDataAccess.save(user);
        SignupOutputData outputData = new SignupOutputData(username);
        outputBoundary.prepareSuccessView(outputData);
    }
}
