package auth.use_case.login;

import auth.data.UserRepository;

public class LoginInteractor {

    private final UserRepository userRepository;

    public LoginInteractor(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
