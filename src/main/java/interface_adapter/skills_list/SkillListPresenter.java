package interface_adapter.skills_list;

import interface_adapter.ViewManagerModel;
import interface_adapter.skills_list.SkillListState;
import interface_adapter.skills_list.SkillListViewModel;
import use_case.course_analysis.AnalysisOutputBoundary;
import use_case.course_analysis.AnalysisOutputData;

/**
 * The Presenter for the Analysis Use Case.
 */
public class SkillListPresenter implements AnalysisOutputBoundary {

    private final SkillListViewModel skillListViewModel;
    private final ViewManagerModel viewManagerModel;

    public SkillListPresenter(ViewManagerModel viewManagerModel,
                              SkillListViewModel skillListViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.skillListViewModel = skillListViewModel;
    }

    @Override
    public void prepareSuccessView(AnalysisOutputData response) {
        // On success, switch to the skillList view.
        final SkillListState skillListState = skillListViewModel.getState();
        skillListState.setSkills(response.getSkills());
        skillListViewModel.notifyStateChanged();

        viewManagerModel.setState(SkillListViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final SkillListState skillListState = skillListViewModel.getState();
        skillListState.setSkillsError(error);
        skillListViewModel.notifyStateChanged();
    }

    @Override
    public void switchToSkillListView() {
        viewManagerModel.setState(SkillListViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }
}
