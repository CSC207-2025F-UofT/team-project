package use_case.set_budget;

/**
 * Input Boundary for the Set Budget use case.
 */
public interface SetBudgetInputBoundary {

    /**
     * Execute the Set Budget use case.
     * @param setBudgetInputData the input data for this use case
     */
    void execute(SetBudgetInputData setBudgetInputData);
    
}
