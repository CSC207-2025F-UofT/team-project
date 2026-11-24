package use_case.signup;

public interface SignUpInputBoundary {
    void execute(String userName, String password, String repeatPassword);
}
