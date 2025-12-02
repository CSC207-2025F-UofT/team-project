package use_case.update_profile;

public interface UpdateUserProfileInputBoundary {
    void openEditPage();
    void saveProfile(UpdateUserProfileInputData data);
    void save(String name, String email, String address, String phone);
}
