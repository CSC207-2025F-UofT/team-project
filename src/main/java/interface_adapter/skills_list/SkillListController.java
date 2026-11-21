package interface_adapter.skills_list;

import entity.Student;
import use_case.course_analysis.AnalysisInputBoundary;
import use_case.course_analysis.AnalysisInputData;

/**
 * Controller for the Analysis Use Case.
 */
public class SkillListController {

    private final AnalysisInputBoundary userAnalysisUseCaseInteractor;

    public SkillListController(AnalysisInputBoundary userAnalysisUseCaseInteractor) {
        this.userAnalysisUseCaseInteractor = userAnalysisUseCaseInteractor;
    }

    /**
     * Executes the Analysis Use Case.
     * @param student student that contains the required courses to be analyzed
     */
    public void execute(Student student) {
        final AnalysisInputData analysisInputData = new AnalysisInputData(student);

        userAnalysisUseCaseInteractor.execute(analysisInputData);
    }

    /**
     * Executes the "switch to SkillView" Use Case.
     */
    public void switchToLoginView() {
        userAnalysisUseCaseInteractor.switchToSkillListView();
    }
}
