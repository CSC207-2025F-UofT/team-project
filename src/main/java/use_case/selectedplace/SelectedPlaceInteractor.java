package use_case.selectedplace;

import data_access.LandmarkDataAccessInterface;
import data_access.UserDataAccessInterface;
import entity.Landmark;
import entity.LandmarkInfo;
import entity.User;
import entity.Visit;

import java.util.List;

public class SelectedPlaceInteractor implements SelectedPlaceInputBoundary {

    private final LandmarkDataAccessInterface landmarkDAO;
    private final UserDataAccessInterface userDAO;
    private final SelectedPlaceOutputBoundary presenter;

    public SelectedPlaceInteractor(LandmarkDataAccessInterface landmarkDAO,
                                   UserDataAccessInterface userDAO,
                                   SelectedPlaceOutputBoundary presenter) {
        this.landmarkDAO = landmarkDAO;
        this.userDAO = userDAO;
        this.presenter = presenter;
    }

    @Override
    public void selectPlace(SelectedPlaceInputData inputData) {
        Landmark landmark = landmarkDAO.findByName(inputData.getLandmarkName());
        if (landmark == null) {
            // in a real app you’d call an error presenter
            System.out.println("[INTERACTOR] Landmark not found: " + inputData.getLandmarkName());
            return;
        }

        LandmarkInfo info = landmark.getLandmarkInfo();

        SelectedPlaceOutputData output = new SelectedPlaceOutputData(
                inputData.getUsername(),
                landmark.getLandmarkName(),
                info.getDescription(),
                info.getAddress(),
                info.getOpenHours()
        );

        presenter.presentPlace(output);
    }

    @Override
    public void checkIn(SelectedPlaceInputData inputData) {
        // to be implemented
    }

    @Override
    public void notes(SelectedPlaceInputData inputData) {
        // to be implemented
    }
}
