package use_case.course_analysis;

import entity.Student;
import entity.StudentFactory;

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
        if ("".equals(analysisInputData.getPassword())) {
            userPresenter.prepareFailView("New password cannot be empty");
        }
        else {
            final Student student = studentFactory.create(analysisInputData.getUsername(),
                    analysisInputData.getPassword());

            userDataAccessObject.changePassword(student);

            final AnalysisOutputData analysisOutputData = new AnalysisOutputData(student.getName());
            userPresenter.prepareSuccessView(analysisOutputData);
        }
    }
}
