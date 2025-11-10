package use_case.grade_team;

import entity.GradingStrategy;
import entity.Team;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradeTeamInteractorTest {

    @Test
    void emptyTeamTest(){
        Team team = new Team("emptyTeam");
        GradingStrategy strategy = new TestStrategy();
        GradeTeamInputData inputData = new GradeTeamInputData(team, strategy);

        GradeTeamOutputBoundary successPresenter = new GradeTeamOutputBoundary(){
            @Override
            public void prepareSuccessView(GradeTeamOutputData gradeTeamOutputData) {
                assertEquals(0., gradeTeamOutputData.getTeamScore());
            }

            @Override
            public void prepareFailureView(String errorMessage) {
                fail("Use Case failure is unexpected.");
            }
        };

        GradeTeamInputBoundary interactor = new GradeTeamInteractor(inputData, successPresenter);
        interactor.execute(inputData);
    }

}
