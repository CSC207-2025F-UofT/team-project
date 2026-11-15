package interface_adapter.evaluate;

import use_case.evaluate.CalculateCheckInputBoundary;

public class CalculateCheckController {
    private final CalculateCheckInputBoundary interactor;

    public CalculateCheckController(CalculateCheckInputBoundary interactor) {
        this.interactor = interactor;
    }

    public boolean canCalculate(String latex) {
        return interactor.canCalculate(latex);
    }
}
