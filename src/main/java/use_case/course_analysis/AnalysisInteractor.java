package use_case.course_analysis;

import entity.Student;
import entity.StudentFactory;

import java.util.ArrayList;

/**
 * The Change Password Interactor.
 */
public class AnalysisInteractor implements AnalysisInputBoundary {
    private final AnalysisStudentDataAccessInterface userDataAccessObject;
    private final AnalysisOutputBoundary userPresenter;
    private final StudentFactory studentFactory;

    public AnalysisInteractor(AnalysisStudentDataAccessInterface changePasswordDataAccessInterface,
                              AnalysisOutputBoundary analysisOutputBoundary,
                              StudentFactory studentFactory) {
        this.userDataAccessObject = changePasswordDataAccessInterface;
        this.userPresenter = analysisOutputBoundary;
        this.studentFactory = studentFactory;
    }

    @Override
    public void execute(AnalysisInputData analysisInputData) {
        if (analysisInputData.getCourses().isEmpty()) {
            userPresenter.prepareFailView("Course list cannot be empty");
        }
        else {
            final Student student = studentFactory.create(analysisInputData.getName(),
                    analysisInputData.getPassword(), new ArrayList<>(),
                    analysisInputData.getCourses());

            userDataAccessObject.changeSkills(student);

            final AnalysisOutputData analysisOutputData = new AnalysisOutputData(student.getSkills());
            userPresenter.prepareSuccessView(analysisOutputData);
        }
    }

    @Override
    public void switchToSkillListView() {

    }
}
