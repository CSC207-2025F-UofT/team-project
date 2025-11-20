package interface_adapter.Profile;

import use_case.profile.ProfileInputBoundary;
import use_case.profile.ProfileInputData;

public class ProfileController {

    private final ProfileInputBoundary interactor;

    public ProfileController(ProfileInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void loadProfile(String username) {
        ProfileInputData data = new ProfileInputData(username);
        interactor.execute(data);
    }
}
