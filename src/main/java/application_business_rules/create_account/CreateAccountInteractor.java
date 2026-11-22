package application_business_rules.create_account;

import entity.Account;
import entity.UserProfile;

/** Creates a financial account and associates it with an existing UserProfile. */
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
        try {
            if (inputData.getAccountName() == null || inputData.getAccountName().isBlank()) {
                presenter.prepareFailView("Account name cannot be empty.");
                return;
            }
            if (inputData.getAccountType() == null) {
                presenter.prepareFailView("Choose an account type.");
                return;
            }
            if (inputData.getInitialBalance() == null || inputData.getInitialBalance().signum() < 0) {
                presenter.prepareFailView("Initial balance cannot be negative.");
                return;
            }
            if (inputData.getCurrencyCode() == null
                    || !inputData.getCurrencyCode().matches("[A-Z]{3}")) {
                presenter.prepareFailView("Enter a valid three-letter currency code.");
                return;
            }

            UserProfile profile = accountDataAccess.getUserProfile(inputData.getUserProfileId());
            if (profile == null) {
                presenter.prepareFailView("User profile could not be found.");
                return;
            }
            if (accountDataAccess.accountNameExists(profile.getId(), inputData.getAccountName())) {
                presenter.prepareFailView("You already have an account with that name.");
                return;
            }

            Account account = new Account(accountDataAccess.nextAccountId(), profile.getId(),
                inputData.getAccountName(), inputData.getAccountType(),
                inputData.getInitialBalance(), inputData.getCurrencyCode());
            profile.addAccount(account);
            accountDataAccess.saveAccount(account, profile);

            presenter.prepareSuccessView(new CreateAccountOutputData(
                account.getAccountID(), account.getAccountName(), false));
        } catch (IllegalArgumentException exception) {
            presenter.prepareFailView(exception.getMessage());
        }
    }
}
