package use_case.signup;

import data.UserRepository;
import entities.User;

public class SignUpInteractor {

    private final UserRepository userRepository;

    public SignUpInteractor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SignUpOutputData execute(SignUpInputData input) {
        String username = input.getUsername();
        String password = input.getPassword();

        if (username == null || username.isBlank()) {
            return new SignUpOutputData(false, "Username is empty!");
        } else if (password == null || password.isBlank()) {
            return new SignUpOutputData(false, "Password is empty!");
        }

        try  {
            User newUser = userRepository.create(username, password);
            return new SignUpOutputData(true, "Account created successfully: " +
                    newUser.getUsername() + "!");
        } catch (IllegalStateException e) {
            return new SignUpOutputData(false, "Username already exists!");
        } catch (Exception e){
            return new SignUpOutputData(false, "Something went wrong!");
        }
    }
}
