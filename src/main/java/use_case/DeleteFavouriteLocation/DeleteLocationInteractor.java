package use_case.DeleteFavouriteLocation;

import entity.user.User;
import use_case.login.LoginUserDataAccessInterface;

/**
 * The DeleteLocation Interactor.
 */
public class DeleteLocationInteractor implements DeleteLocationInputBoundary{
    private final LoginUserDataAccessInterface userDataAccessObject;
    private final DeleteLocationOutputBoundary presenter;

    public DeleteLocationInteractor(LoginUserDataAccessInterface userRepository,
                                    DeleteLocationOutputBoundary presenter) {
        this.userDataAccessObject = userRepository;
        this.presenter = presenter;
    }

    @Override
    public void execute(DeleteLocationInputData inputData) {
        User user = userDataAccessObject.get(userDataAccessObject.getCurrentUsername());
        if (user == null) {
            presenter.prepareFailView("User not found");
            return;
        }

        boolean removed = user.getLocations().removeIf(loc -> loc.getName().equals(inputData.getCityName()));
        if (!removed) {
            presenter.prepareFailView("Location not found");
            return;
        }

        // saves the update user with updated locations list
        userDataAccessObject.save(user);
        DeleteLocationOutputData outputData = new DeleteLocationOutputData(user.getLocations());
        presenter.prepareSuccessView(outputData);
    }
}
