package user_case.SignUp;

import entity.User;
import entity.UserFactory;

public class SignupInteractor implements SignupInputBoundary {
    private final SignupUserDataAccessInterface userDataAccessObject;
    private final SignupOutputBoundary userPresenter;
    private final UserFactory userFactory;

    public SignupInteractor(SignupUserDataAccessInterface signupUserDataAccessInterface,
                            SignupOutputBoundary signupOutputBoundary,
                            UserFactory userFactory){
        this.userDataAccessObject = signupUserDataAccessInterface;
        this.userPresenter = signupOutputBoundary;
        this.userFactory = userFactory;
    }
    @Override
    public void execute(SignupInputData signupInputData){
        if(userDataAccessObject.existByName(signupInputData.getUsername())){
            userPresenter.prepareFailView("User exists");
        } else if (!signupInputData.getPassword().equals(signupInputData.getRepeatPassword())){
            userPresenter.prepareFailView("Pass don't match");
        } else if ("".equals(signupInputData.getPassword())) {
            userPresenter.prepareFailView("Pass can't be empty");
        } else if ("".equals(signupInputData.getUsername())){
            userPresenter.prepareFailView("Username can't be empty");
        } else{
            final User user = userFactory.create(signupInputData.getUsername(), signupInputData.getPassword());
            userDataAccessObject.save(user);

            final SignupOutputData signupOutputData = new SignupOutputData(user.getName());
            userPresenter.prepareSuccessView(signupOutputData);
        }
    }

    @Override
    public void switchToLoginView() {
        userPresenter.switchToLoginView();
    }

}
