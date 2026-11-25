package interface_adapter.create_account;

import use_case.create_account.CreateAccountInputBoundary;
import use_case.create_account.CreateAccountInputData;
import entity.AccountType;
import java.math.BigDecimal;

/** Converts raw UI values into the Create Account use case's Input Data. */
public final class CreateAccountController {
    private final CreateAccountInputBoundary inputBoundary;

    public CreateAccountController(CreateAccountInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    public void execute(int userProfileId, String accountName,
                        AccountType accountType, String initialBalance,
                        String currencyCode) {
        CreateAccountInputData inputData = new CreateAccountInputData(
            userProfileId,
            accountName == null ? null : accountName.trim(),
            accountType,
            new BigDecimal(initialBalance),
            currencyCode == null ? null : currencyCode.trim().toUpperCase()
        );
        inputBoundary.execute(inputData);
    }
}
