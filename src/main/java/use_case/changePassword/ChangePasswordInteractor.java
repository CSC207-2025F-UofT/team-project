package use_case.changePassword;

import data_access.FileUserDataAccessObjectWithLocations;
import entity.user.User;
import entity.user.UserFactory;

/**
 * The Change Password Interactor.
 */
public class ChangePasswordInteractor implements ChangePasswordInputBoundary {
    private final ChangePasswordUserDataAccessInterface userDataAccessObject;
    private final ChangePasswordOutputBoundary userPresenter;
    private final UserFactory userFactory;
    private FileUserDataAccessObjectWithLocations fileUserDataAccessObjectWithLocations;


    public ChangePasswordInteractor(ChangePasswordUserDataAccessInterface changePasswordDataAccessInterface,
                                    ChangePasswordOutputBoundary changePasswordOutputBoundary,
                                    UserFactory userFactory,
                                    FileUserDataAccessObjectWithLocations fileUserDataAccessObjectWithLocations) {
        this.userDataAccessObject = changePasswordDataAccessInterface;
        this.userPresenter = changePasswordOutputBoundary;
        this.userFactory = userFactory;
        this.fileUserDataAccessObjectWithLocations = fileUserDataAccessObjectWithLocations;
    }

    @Override
    public void execute(ChangePasswordInputData changePasswordInputData) {
        String userName = fileUserDataAccessObjectWithLocations.getCurrentUsername();
        User currentUser = fileUserDataAccessObjectWithLocations.get(userName);
        String password = currentUser.getPassword();
        if ("".equals(changePasswordInputData.getPassword())) {
            userPresenter.prepareFailView("New password cannot be empty");
        }
        else if (!changePasswordInputData.getPassword().equals(changePasswordInputData.getRepeatPassword())) {
            userPresenter.prepareFailView("two password don't match");}

        else if (password.equals(changePasswordInputData.getPassword())) {
            userPresenter.prepareFailView("New password cannot be same");


        } else {
            final User user = userFactory.create(userName,
                    changePasswordInputData.getPassword());

            userDataAccessObject.changePassword(user);

            final ChangePasswordOutputData changePasswordOutputData = new ChangePasswordOutputData(user.getName());
            userPresenter.prepareSuccessView(changePasswordOutputData);
        }
    }
}
