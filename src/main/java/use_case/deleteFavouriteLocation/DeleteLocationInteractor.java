package use_case.deleteFavouriteLocation;

import entity.user.User;

/**
 * The DeleteLocation Interactor.
 */
public class DeleteLocationInteractor implements DeleteLocationInputBoundary{
    private final DeleteLocationUserDataAccessInterface userDataAccessObject;
    private final DeleteLocationOutputBoundary presenter;

    public DeleteLocationInteractor(DeleteLocationOutputBoundary presenter,
                                    DeleteLocationUserDataAccessInterface userDataAccessObject) {
        this.userDataAccessObject = userDataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute(DeleteLocationInputData inputData) {
        // Gets the current logged-in user information
        User user = userDataAccessObject.get(userDataAccessObject.getCurrentUsername());
        if (user == null) {
            presenter.prepareFailView("User not found");
            return;
        }

        if (!user.getLocations().contains(inputData.getCityName())) {
            presenter.prepareFailView("Location was not favourite");
            return;
        }

        // If user exists, and the location was a favourite location, remove the location
        user.removeLocation(inputData.getCityName());

        userDataAccessObject.save(user);
        DeleteLocationOutputData outputData = new DeleteLocationOutputData(user.getLocations());
        presenter.prepareSuccessView(outputData);
    }
}
