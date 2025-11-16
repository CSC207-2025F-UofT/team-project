package Brandon.interfaceAdapter;

import Brandon.useCase.SetBudgetInputBoundary;
import Brandon.useCase.SetBudgetInputData;

public class SetBudgetController {

    private final SetBudgetInputBoundary interactor;

    public SetBudgetController (SetBudgetInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void setBudget(String month, float limit) {
        SetBudgetInputData inputData= new SetBudgetInputData(month, limit);
        interactor.execute(inputData);
    }
}
