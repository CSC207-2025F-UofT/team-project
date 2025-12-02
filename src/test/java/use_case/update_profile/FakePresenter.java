package use_case.update_profile;

public class FakePresenter implements UpdateUserProfileOutputBoundary {

    public boolean editShown = false;
    public boolean successShown = false;
    public boolean failShown = false;

    public String failMessage = null;

    @Override
    public void showEditProfile() {
        editShown = true;
    }

    @Override
    public void showProfileUpdateSuccess() {
        successShown = true;
    }

    @Override
    public void showProfileUpdateFailure(String message) {
        failShown = true;
        failMessage = message;
    }
}
