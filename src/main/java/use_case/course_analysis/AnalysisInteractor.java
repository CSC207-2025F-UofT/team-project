package use_case.course_analysis;

import entity.Student;
import entity.StudentFactory;

import java.util.ArrayList;

/**
 * The Change Password Interactor.
 */
public class AnalysisInteractor implements AnalysisInputBoundary {
    private final AnalysisDataAccessInterface userDataAccessObject;
    private final SkillDataAccessInterface skillDataAccessObject;
    private final AnalysisOutputBoundary userPresenter;
    private final StudentFactory studentFactory;

    public AnalysisInteractor(AnalysisDataAccessInterface userDataAccessInterface,
                              SkillDataAccessInterface skillDataAccessInterface,
                              AnalysisOutputBoundary analysisOutputBoundary,
                              StudentFactory studentFactory) {
        this.userDataAccessObject = userDataAccessInterface;
        this.skillDataAccessObject = skillDataAccessInterface;
        this.userPresenter = analysisOutputBoundary;
        this.studentFactory = studentFactory;
    }

    @Override
    public void execute(AnalysisInputData analysisInputData) {
        if (analysisInputData.getCourses().isEmpty()) {
            userPresenter.prepareFailView("Course list cannot be empty");
        }
        else {
            ArrayList<String> skills = new ArrayList<>();
            for (String code : analysisInputData.getCourses()){
                String description = userDataAccessObject.getCourseDescription(code);
                skills.add(userDataAccessObject.getSkill(description));
            }

            final Student student = studentFactory.create(analysisInputData.getName(),
                    analysisInputData.getPassword(), skills,
                    analysisInputData.getCourses());

            skillDataAccessObject.changeSkills(student);

            final AnalysisOutputData analysisOutputData = new AnalysisOutputData(student.getSkills());
            userPresenter.prepareSuccessView(analysisOutputData);
        }
    }

    @Override
    public void switchToSkillListView() {
        return;
    }
}
