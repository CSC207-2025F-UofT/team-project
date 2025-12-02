package use_case.update_profile;

import dataAccess.FileUserDataAccessObject;
import entity.User;

public class UpdateUserProfileInteractor implements UpdateUserProfileInputBoundary {

    private final FileUserDataAccessObject userDAO;
    private final UpdateUserProfileOutputBoundary presenter;

    public UpdateUserProfileInteractor(FileUserDataAccessObject userDataAccessObject,
                                       UpdateUserProfileOutputBoundary presenter) {
        this.userDAO = userDataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void openEditPage() {
        presenter.showEditProfile();
    }

    @Override
    public void saveProfile(UpdateUserProfileInputData data) {
        User u = userDAO.getCurrentUser();
        if (u != null) {
            User updated = new User(
                    u.getId(),
                    data.name,
                    data.address,
                    u.getIdType(),
                    u.getPhoneNumber(),
                    data.email,
                    u.getUsername(),
                    u.getPassword(),
                    u.getUserType()
            );
            userDAO.save(updated);
        }
        presenter.showProfileUpdateSuccess();
    }
    
    @Override
    public void save(String name, String email, String address, String phone) {

        User user = userDAO.getCurrentUser();

        User updated = new User(
                user.getId(),
                name,
                address,
                user.getIdType(),
                Integer.parseInt(phone),
                email,
                user.getUsername(),
                user.getPassword(),
                user.getUserType()
        );

        userDAO.updateUser(updated);

//        presenter.prepareSuccessView(updated);
    }

}
