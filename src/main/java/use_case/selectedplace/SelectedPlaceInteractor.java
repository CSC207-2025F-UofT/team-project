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
        String username = inputData.getUsername();
        String landmarkName = inputData.getLandmarkName();

        // 1. get user
        User user = userDAO.get(username);
        if (user == null) {
            System.out.println("[INTERACTOR] Cannot check in: user not found " + username);
            return;
        }

        // 2. get landmark
        Landmark landmark = landmarkDAO.findByName(landmarkName);
        if (landmark == null) {
            System.out.println("[INTERACTOR] Cannot check in: landmark not found " + landmarkName);
            return;
        }

        // 3. append visit
        List<Visit> visits = user.getVisits();
        if (visits == null) {
            System.out.println("[INTERACTOR] User visits list is null; cannot record visit.");
        } else {
            visits.add(new Visit(landmark));
            System.out.println("[INTERACTOR] Added visit for user " + username +
                    " at landmark " + landmarkName);
        }

        // 4. save updated user (overwrites existing entry)
        userDAO.save(user);

        // 5. Optionally re-present the same place data so the view stays in sync
        LandmarkInfo info = landmark.getLandmarkInfo();
        SelectedPlaceOutputData output = new SelectedPlaceOutputData(
                username,
                landmark.getLandmarkName(),
                info.getDescription(),
                info.getAddress(),
                info.getOpenHours()
        );
        presenter.presentPlace(output);
    }
}
