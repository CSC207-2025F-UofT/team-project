package interface_adapter.team_builder;

import interface_adapter.ViewModel;

public class TeamBuilderViewModel extends ViewModel<TeamBuilderState> {

    public static final String TITLE_LABEL = "Team Builder";
    public static final String TEAM_NAME_LABEL = "Team Name";
    public static final String SAVE_BUTTON_LABEL = "Save";

    public TeamBuilderViewModel() {
        super("team builder");
        setState(new TeamBuilderState());
    }
}
