package use_case.profile;

import entity.User;
import data_access.UserDataAccessInterface;

public class ProfileInteractor implements ProfileInputBoundary {

    private final UserDataAccessInterface userDAO;
    private final ProfileOutputBoundary presenter;

    public ProfileInteractor(UserDataAccessInterface userDAO,
                             ProfileOutputBoundary presenter) {
        this.userDAO = userDAO;
        this.presenter = presenter;
    }

    @Override
    public void execute(ProfileInputData inputData) {
        User user = userDAO.get(inputData.getUsername());

        ProfileOutputData out = new ProfileOutputData(
                user.getUsername(),
                user.getBalance(),
                user.getBets(),
                user.getGamesPlayed());

        presenter.prepareSuccessView(out);
    }
}
