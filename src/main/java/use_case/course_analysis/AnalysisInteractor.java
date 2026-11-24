package use_case.course_analysis;

import entity.Student;
import entity.StudentFactory;

import java.util.ArrayList;

/**
 * The Course Analysis Interactor.
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
        final Student student = skillDataAccessObject.get(analysisInputData.getUsername());
        if (student == null) {
            userPresenter.prepareFailView("Please log in first.");
        }
        else if (student.getCourses().isEmpty()) {
            userPresenter.prepareFailView("Course list cannot be empty");
        }
        else {
            ArrayList<String> skills = new ArrayList<>();
            for (String code : student.getCourses()) {
                String description = userDataAccessObject.getCourseDescription(code);
                skills.add(userDataAccessObject.getSkill(description));
            }

            final Student updatedStudent = studentFactory.create(student.getName(),
                    student.getPassword(), student.getCourses(), skills);

            skillDataAccessObject.changeSkills(updatedStudent);

            final AnalysisOutputData analysisOutputData = new AnalysisOutputData(updatedStudent.getSkills());
            userPresenter.prepareSuccessView(analysisOutputData);
        }
    }

    @Override
    public void switchToLoggedInView() {
        userPresenter.switchToLoggedInView();
    }
}
