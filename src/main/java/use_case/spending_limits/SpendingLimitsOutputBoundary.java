package use_case.spending_limits;

import java.util.Map;

public interface SpendingLimitsOutputBoundary {
    void prepareSuccessView();
    void prepareFailView(String error);
    void presentLimits(Map<String, Double> limits);
    void presentSaveSuccess();
    void presentSaveFailure(String error);
}
