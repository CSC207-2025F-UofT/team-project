package user_case.change_password;
import entity.User;
import entity.UserFactory;

public class ChangePasswordInteractor implements ChangePasswordInputBoundary{
    private final ChangePasswordUserDataAccessInterface userDataAccessObject;
    private final ChangePasswordOutputBoundary userPresenter;
    private final UserFactory userFactory;

    public ChangePasswordInteractor(ChangePasswordUserDataAccessInterface userDataAccessInterface,
                                    ChangePasswordOutputBoundary changePasswordOutputBoundary,
                                    UserFactory userFactory){
        this.userDataAccessObject = userDataAccessInterface;
        this.userPresenter = changePasswordOutputBoundary;
        this.userFactory = userFactory;
    }
    @Override
    public void execute(ChangePasswordInputData changePasswordInputData){
        if("".equals(changePasswordInputData.getPassword())){
            userPresenter.PrepareFailView("Password can't be empty");
        } else{
            final User user = userFactory.create(changePasswordInputData.getUsername(), changePasswordInputData.getPassword());

            userDataAccessObject.changePassword(user);

            final ChangePasswordOutputData changePasswordOutputData = new ChangePasswordOutputData(user.getName());
            userPresenter.PrepareSuccessView(changePasswordOutputData);
        }
    }

}
