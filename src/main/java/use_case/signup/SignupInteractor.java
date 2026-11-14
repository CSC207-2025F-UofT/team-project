package use_case.signup;

import entity.User;
import entity.UserFactory;

public class SignupInteractor implements SignupInputBoundary{
    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory userFactory;

    public SignupInteractor(SignupUserDataAccessInterface signupDataAccessInterface,
                            SignupOutputBoundary signupOutputBoundary,
                            UserFactory userFactory) {
        this.userDataAccessObject = signupDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
    }

    @Override
    public void execute(SignupInputData signupInputData){
        final String username = signupInputData.getUsername();
        final String password = signupInputData.getPassword();
        if (userDataAccessObject.existsByName(username)){
            userPresenter.prepareFailView("User already exists.");
        }
        else if (username.contains(",") || password.contains(",")) {
            userPresenter.prepareFailView("Username and password cannot contain commas.");
        }
        else if ("".equals(password)){
            userPresenter.prepareFailView("Password cannot be empty");
        }
        else if ("".equals(username)){
            userPresenter.prepareFailView("Username cannot be empty");
        }
        else {
            final User user = userFactory.create(username, password);
            userDataAccessObject.save(user);

            final SignupOutputData signupOutputData = new SignupOutputData(user.getName());
            userPresenter.prepareSuccessView(signupOutputData);
        }
    }
}
