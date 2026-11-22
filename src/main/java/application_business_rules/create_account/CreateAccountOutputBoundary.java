package application_business_rules.create_account;

/** Output Boundary implemented by the Create Account Presenter. */
public interface CreateAccountOutputBoundary {
    void prepareSuccessView(CreateAccountOutputData outputData);
    void prepareFailView(String error);
}
