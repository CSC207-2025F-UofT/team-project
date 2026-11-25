package user_case.change_password;

public interface ChangePasswordOutputBoundary {
    void PrepareSuccessView(ChangePasswordOutputData changePasswordOutputData);
    void PrepareFailView(String errorMessage);
}
