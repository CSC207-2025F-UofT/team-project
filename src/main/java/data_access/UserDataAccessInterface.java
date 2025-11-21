package data_access;

import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

public interface UserDataAccessInterface extends LoginUserDataAccessInterface,
        SignupUserDataAccessInterface {
    // Composite interface for easier code writing
}
