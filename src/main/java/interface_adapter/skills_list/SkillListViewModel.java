package interface_adapter.skills_list;

import java.util.ArrayList;
import java.util.List;

/**
 * The ViewModel for the Signup View.
 */
public class SkillListViewModel {

    public static final String TITLE_LABEL = "Skill List View";
    public static final String SIGNUP_BUTTON_LABEL = "Analyze";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";

    private final List<SkillListViewModelListener> listeners = new ArrayList<>();

    public static final String VIEW_NAME = "skill list";

    private SkillListState state = new SkillListState();

    public SkillListState getState() {
        return this.state;
    }

    public void setState(SkillListState state) {
        this.state = state;
    }

    public void addListener(SkillListViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (SkillListViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
