package use_case.signup;

import interface_adapter.DataAccessInterface;

/** Original team signup interactor, relocated from application_business_rules. */
public class SignUpInteractor implements SignUpInputBoundary {
    private final DataAccessInterface dataAccessObject;
    private SignUpOutputBoundary presenter;

    public SignUpInteractor(DataAccessInterface dataAccessObject) {
        this.dataAccessObject = dataAccessObject;
    }

    @Override
    public void execute(String userName, String password, String repeatPassword) {
        if (dataAccessObject.existUserName(userName)) {
            presenter.prepareFailureView("Name already exists");
        } else if (password.equals(repeatPassword)) {
            presenter.prepareFailureView("Passwords are not same");
        } else {
            SignUpOutputData signUpOutputData = new SignUpOutputData(userName, password);
            presenter.prepareSuccessView(signUpOutputData);
            dataAccessObject.save();
        }
    }
}
