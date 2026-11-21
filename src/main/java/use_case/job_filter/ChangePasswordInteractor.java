package use_case.job_filter;

import entity.Student;
import entity.StudentFactory;

import java.util.ArrayList;

/**
 * The Change Password Interactor.
 */
public class ChangePasswordInteractor implements ChangePasswordInputBoundary {
    private final ChangePasswordUserDataAccessInterface userDataAccessObject;
    private final ChangePasswordOutputBoundary userPresenter;
    private final StudentFactory studentFactory;

    public ChangePasswordInteractor(ChangePasswordUserDataAccessInterface changePasswordDataAccessInterface,
                                    ChangePasswordOutputBoundary changePasswordOutputBoundary,
                                    StudentFactory studentFactory) {
        this.userDataAccessObject = changePasswordDataAccessInterface;
        this.userPresenter = changePasswordOutputBoundary;
        this.studentFactory = studentFactory;
    }

    @Override
    public void execute(ChangePasswordInputData changePasswordInputData) {
        if ("".equals(changePasswordInputData.getPassword())) {
            userPresenter.prepareFailView("New password cannot be empty");
        }
        else {
            final Student student = studentFactory.create(changePasswordInputData.getUsername(),
                    changePasswordInputData.getPassword(), new ArrayList<>(), new ArrayList<>());

            userDataAccessObject.changePassword(student);

            final ChangePasswordOutputData changePasswordOutputData = new ChangePasswordOutputData(student.getName());
            userPresenter.prepareSuccessView(changePasswordOutputData);
        }
    }
}
