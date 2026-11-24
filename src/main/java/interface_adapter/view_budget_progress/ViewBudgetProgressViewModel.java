package interface_adapter.view_budget_progress;
import java.util.ArrayList;
import java.util.List;
public class ViewBudgetProgressViewModel {
    public static final String VIEW_NAME = "budget progress";
    private final List<Runnable> listeners = new ArrayList<>();
    private ViewBudgetProgressState state = new ViewBudgetProgressState("", 0, false);
    public ViewBudgetProgressState getState() { return state; }
    public void setState(ViewBudgetProgressState state) { this.state = state; }
    public void addListener(Runnable listener) { listeners.add(listener); }
    public void notifyStateChanged() { for (Runnable listener : listeners) listener.run(); }
}
