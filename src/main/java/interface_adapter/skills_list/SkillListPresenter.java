package interface_adapter.skills_list;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.course_analysis.AnalysisOutputBoundary;
import use_case.course_analysis.AnalysisOutputData;

/**
 * The Presenter for the Course Analysis Use Case.
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
        final SkillListState skillListState = skillListViewModel.getState();
        skillListState.setSkills(response.getSkills());
        skillListState.setSkillsError(null);
        skillListViewModel.notifyStateChanged();

        viewManagerModel.setState(SkillListViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

    @Override
    public void prepareFailView(String error) {
        // Still switch to the skill list so the student can see the error.
        final SkillListState skillListState = skillListViewModel.getState();
        skillListState.setSkillsError(error);
        skillListViewModel.notifyStateChanged();

        viewManagerModel.setState(SkillListViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }

    @Override
    public void switchToLoggedInView() {
        viewManagerModel.setState(LoggedInViewModel.VIEW_NAME);
        viewManagerModel.notifyViewChanged();
    }
}
