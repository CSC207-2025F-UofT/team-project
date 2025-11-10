package use_case.grade_team;

import entity.GradingStrategy;
import entity.Pokemon;
import entity.Team;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GradeTeamInteractorTest {

    @Test
    void emptyTeamTest(){
        Team team = new Team("emptyTeam");
        GradingStrategy strategy = new TestStrategy();
        GradeTeamInputData inputData = new GradeTeamInputData("emptyTeam", strategy);

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

    @Test
    void fullTeamTest(){
        Team team = new Team("fullTeam");
        for(int i = 0; i < 6; i++){
            team.setPokemon(new Pokemon(), i);
        }
        GradingStrategy strategy = new TestStrategy();
        GradeTeamInputData inputData = new GradeTeamInputData(team, strategy);

        GradeTeamOutputBoundary successPresenter = new GradeTeamOutputBoundary(){
            @Override
            public void prepareSuccessView(GradeTeamOutputData gradeTeamOutputData) {
                assertEquals(6., gradeTeamOutputData.getTeamScore());
            }

            @Override
            public void prepareFailureView(String errorMessage) {
                fail("Use Case failure is unexpected.");
            }
        };

        GradeTeamInputBoundary interactor = new GradeTeamInteractor(inputData, successPresenter);
        interactor.execute(inputData);
    }

    @Test
    void firstHalfTeamTest(){
        Team team = new Team("fullTeam");
        for(int i = 0; i < 3; i++){
            team.setPokemon(new Pokemon(), i);
        }
        GradingStrategy strategy = new TestStrategy();
        GradeTeamInputData inputData = new GradeTeamInputData(team, strategy);

        GradeTeamOutputBoundary successPresenter = new GradeTeamOutputBoundary(){
            @Override
            public void prepareSuccessView(GradeTeamOutputData gradeTeamOutputData) {
                assertEquals(3., gradeTeamOutputData.getTeamScore());
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
