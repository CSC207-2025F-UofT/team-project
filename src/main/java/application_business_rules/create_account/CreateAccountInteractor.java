package application_business_rules.create_account;

/**
 * Create Account use case.
 *
 * TODO: Complete after the legacy Account and UserProfile entities have been
 * converted to persistence-free domain entities. The Interactor must:
 * 1. validate input and ownership,
 * 2. obtain the UserProfile through AccountDataAccessInterface,
 * 3. create a financial Account (not a login identity),
 * 4. associate it with that UserProfile,
 * 5. save both through the data access interface,
 * 6. send Output Data to the Output Boundary.
 */
public final class CreateAccountInteractor implements CreateAccountInputBoundary {
    private final AccountDataAccessInterface accountDataAccess;
    private final CreateAccountOutputBoundary presenter;

    public CreateAccountInteractor(AccountDataAccessInterface accountDataAccess,
                                   CreateAccountOutputBoundary presenter) {
        this.accountDataAccess = accountDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(CreateAccountInputData inputData) {
        throw new UnsupportedOperationException(
            "Complete after the domain entities are migrated from the legacy persistence-coupled model."
        );
    }
}
