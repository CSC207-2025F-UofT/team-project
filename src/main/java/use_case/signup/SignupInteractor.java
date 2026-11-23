package use_case.signup;
import entities.User;
import entities.RegularUser;
import entities.Researcher;
// import entity.UserFactory; dont have and dont know if we have to have

public class SignupInteractor implements SignupInputBoundary {
    private final SignupUserDataAccessInterface dao;
    private final SignupOutputBoundary presenter;

    public SignupInteractor(SignupUserDataAccessInterface dao, SignupOutputBoundary presenter) {
        this.dao = dao;
        this.presenter = presenter;
    }

    @Override
    public void execute(SignupInputData inputData) {
        final String username = inputData.getUsername() == null ? "" : inputData.getUsername().trim();

        if (username.isEmpty()) {
            presenter.prepareFailView("Username cannot be empty.");
        }
        else if (dao.existsByName(username)) {
            presenter.prepareFailView("Username already exists.");
        }
        else {
            final User user = inputData.isResearcher()
                    ? new Researcher(username) : new RegularUser(username); // If isResearcher() is true, make a Researcher; otherwise make a RegularUser, and assign the result to user.
            dao.save(user);
            presenter.prepareSuccessView(new SignupOutputData(username, user.getRole()));
        }
    }
    @Override
    public void switchToLoginView() {
        presenter.switchToLoginView();
    }
}