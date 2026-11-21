package interface_adapter.daily_budget;

import java.util.ArrayList;
import java.util.List;

/** Holds the budget screen's state and notifies its Swing view. */
public final class DailyBudgetViewModel {
  public static final String VIEW_NAME = "daily budget";

  private final List<DailyBudgetViewModelListener> listeners = new ArrayList<>();
  private DailyBudgetState state = new DailyBudgetState(List.of(), null, "", false);

  public DailyBudgetState getState() {
    return state;
  }

  void setState(DailyBudgetState state) {
    this.state = state;
  }

  public void addListener(DailyBudgetViewModelListener listener) {
    listeners.add(listener);
  }

  void notifyStateChanged() {
    for (DailyBudgetViewModelListener listener : listeners) {
      listener.stateChanged();
    }
  }
}
