package interface_adapter.set_budget;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.set_budget.SetBudgetOutputBoundary;
import use_case.set_budget.SetBudgetOutputData;

/** 
 * The Presenter for the Set Budget use case. 
 */
public class SetBudgetPresenter implements SetBudgetOutputBoundary {
    private final ViewManagerModel viewManagerModel;
    private final SetBudgetViewModel setBudgetViewModel;
    private final LoggedInViewModel loggedInViewModel;

    public SetBudgetPresenter(ViewManagerModel viewManagerModel, SetBudgetViewModel setBudgetViewModel, LoggedInViewModel loggedInViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.setBudgetViewModel = setBudgetViewModel;
        this.loggedInViewModel = loggedInViewModel;
    }

    @Override
    public void prepareSuccessView(SetBudgetOutputData outputData) {
        final SetBudgetState state = setBudgetViewModel.getState();
        state.setDailyLimit(outputData.getDailyLimitText());
        state.setBalance(outputData.getBalanceText());
        state.setErrorMessage("");
        state.setSuccessMessage("Budget saved.");
        setBudgetViewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final SetBudgetState state = setBudgetViewModel.getState();
        state.setErrorMessage(error);
        state.setSuccessMessage("");
        setBudgetViewModel.notifyStateChanged();
    }
}