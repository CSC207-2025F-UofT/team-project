package interface_adapter.create_account;

import application_business_rules.create_account.CreateAccountOutputBoundary;
import application_business_rules.create_account.CreateAccountOutputData;

/** Converts use-case Output Data into values the View can display. */
public final class CreateAccountPresenter implements CreateAccountOutputBoundary {
    private final CreateAccountViewModel viewModel;

    public CreateAccountPresenter(CreateAccountViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(CreateAccountOutputData outputData) {
        viewModel.setAccountName(outputData.getAccountName());
        viewModel.setMessage("Account created successfully.");
        viewModel.setSuccessful(true);
    }

    @Override
    public void prepareFailView(String error) {
        viewModel.setMessage(error);
        viewModel.setSuccessful(false);
    }
}
