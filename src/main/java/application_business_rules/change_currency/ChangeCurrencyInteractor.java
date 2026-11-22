package application_business_rules.change_currency;

/** TODO: Gary migrates the existing currency conversion behaviour into this Interactor. */
public final class ChangeCurrencyInteractor implements ChangeCurrencyInputBoundary {
    private final CurrencyDataAccessInterface dataAccess;
    private final ChangeCurrencyOutputBoundary presenter;

    public ChangeCurrencyInteractor(CurrencyDataAccessInterface dataAccess,
                                    ChangeCurrencyOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(ChangeCurrencyInputData inputData) {
        throw new UnsupportedOperationException("Change Currency use case not implemented yet.");
    }
}
