package use_case.create_account;

/** Input Boundary called by the Create Account Controller. */
public interface CreateAccountInputBoundary {
    void execute(CreateAccountInputData inputData);
}
