package use_case.SaveFavouriteLocation;

import api.geocodingapi.CoordinatesFetcher;
import entity.user.User;
import entity.location.*;
import use_case.login.LoginUserDataAccessInterface;

/**
 * The SaveLocation Interactor.
 */
public class SaveLocationInteractor implements SaveLocationInputBoundary {
    private final LoginUserDataAccessInterface userDataAccessObject;
    private final SaveLocationOutputBoundary presenter;
    private final LocationFactory locationFactory;

    public SaveLocationInteractor(LoginUserDataAccessInterface userRepository,
                                    SaveLocationOutputBoundary presenter,
                                  LocationFactory locationFactory) {
        this.userDataAccessObject = userRepository;
        this.presenter = presenter;
        this.locationFactory = locationFactory;
    }

    @Override
    public void execute(SaveLocationInputData inputData) throws CoordinatesFetcher.CityNotFoundException {
        User user = userDataAccessObject.get(userDataAccessObject.getCurrentUsername());
        if (user == null) {
            presenter.prepareFailView("User not found");
            return;
        }

        // Check if the location has already been saved
        boolean exists = user.getLocations().stream()
                .anyMatch(loc -> loc.getName().equals(inputData.getCityName()));

        if (exists) {
            presenter.prepareFailView("Location already saved");
            return;
        }

        // saves the update user with updated locations list
        user.addLocation(locationFactory.create(inputData.getCityName()));

        userDataAccessObject.save(user);
        SaveLocationOutputData outputData = new SaveLocationOutputData(user.getLocations());
        presenter.prepareSuccessView(outputData);
    }
}
