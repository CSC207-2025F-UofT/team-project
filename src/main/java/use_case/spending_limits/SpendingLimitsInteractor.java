package use_case.spending_limits;

import java.util.Map;

public class SpendingLimitsInteractor implements SpendingLimitsInputBoundary {
    private final SpendingLimitsOutputBoundary presenter;
    private final SpendingLimitsDataAccessInterface spendingLimitsDataAccessInterface;

    public SpendingLimitsInteractor(SpendingLimitsOutputBoundary presenter,
                                    SpendingLimitsDataAccessInterface spendingLimitsDataAccessInterface) {
        this.presenter = presenter;
        this.spendingLimitsDataAccessInterface = spendingLimitsDataAccessInterface;
    }

    @Override
    public void execute() {

    }

    @Override
    public void loadLimits(String username) {
        Map<String, Double> limits = spendingLimitsDataAccessInterface.getSpendingLimitsByUsername(username);
        presenter.presentLimits(limits);
    }

    @Override
    public void saveLimits(String username, Map<String, Double> limits) {
        try {
            spendingLimitsDataAccessInterface.saveUserSpendingLimits(username, limits);
            presenter.presentSaveSuccess();
        } catch (Exception e) {
            presenter.presentSaveFailure(e.getMessage());
        }
    }
}
