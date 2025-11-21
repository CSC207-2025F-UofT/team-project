package Brandon.useCase;

import Brandon.entities.Budget;

public interface SetBudgetDataAccessInterface {
    Budget getBudgetForMonth(String month);
    void saveBudget(Budget budget);
}
