package use_case.grade_team;

public class GradeTeamInteractor implements GradeTeamInputBoundary {

    private final GradeTeamInputData userDataAccessObject;
    private final GradeTeamOutputBoundary userPresenter;

    public GradeTeamInteractor(GradeTeamInputData userDataAccessObject, GradeTeamOutputBoundary userPresenter) {
        this.userDataAccessObject = userDataAccessObject;
        this.userPresenter = userPresenter;
    }

    @Override
    public void execute(GradeTeamInputData gradeTeamInputData) {
        try{
            float teamScore = gradeTeamInputData.getStrategy().execute(gradeTeamInputData.getTeam());
            GradeTeamOutputData gradeTeamOutputData = new GradeTeamOutputData(teamScore);
            userPresenter.prepareSuccessView(gradeTeamOutputData);
        }catch(Exception e){
            userPresenter.prepareFailureView("Some exception occurred");
        }
    }
}
