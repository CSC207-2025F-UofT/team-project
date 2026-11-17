package interface_adapter.add_expense;

/**
 * Observer interface implemented by {@link view.AddExpenseView} to react when {@link
 * AddExpenseViewModel} updates its {@link AddExpenseState}. Mirrors {@link
 * interface_adapter.login.LoginViewModelListener}.
 */
public interface AddExpenseViewModelListener {

    /** Called when the {@link AddExpenseViewModel} state has changed. */
    void stateChanged();
}