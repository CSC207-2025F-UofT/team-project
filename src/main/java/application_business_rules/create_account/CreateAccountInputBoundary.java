package application_business_rules.create_account;

/** Input Boundary called by the Create Account Controller. */
public interface CreateAccountInputBoundary {
    void execute(CreateAccountInputData inputData);
}
