package interface_adapter.skills_list;

import use_case.course_analysis.AnalysisInputBoundary;
import use_case.course_analysis.AnalysisInputData;

/**
 * Controller for the Course Analysis Use Case.
 */
public class SkillListController {

    private final AnalysisInputBoundary userAnalysisUseCaseInteractor;

    public SkillListController(AnalysisInputBoundary userAnalysisUseCaseInteractor) {
        this.userAnalysisUseCaseInteractor = userAnalysisUseCaseInteractor;
    }

    /**
     * Executes the Course Analysis Use Case.
     * @param username the logged-in student whose courses are analyzed
     */
    public void execute(String username) {
        userAnalysisUseCaseInteractor.execute(new AnalysisInputData(username));
    }

    /**
     * Executes the "switch to LoggedInView" Use Case.
     */
    public void switchToLoggedInView() {
        userAnalysisUseCaseInteractor.switchToLoggedInView();
    }
}
