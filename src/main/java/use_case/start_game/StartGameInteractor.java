package use_case.start_game;
import entity.Team;

/**
 * Start game interactor
 */
public class StartGameInteractor implements StartGameInputBoundary {
    private final StartGameDataAccessInterface startGameDataAccessInterface;
    private final StartGameOutputBoundary startGamePresenter;

    public StartGameInteractor(StartGameDataAccessInterface startGameDataAccessInterface,
                               StartGameOutputBoundary startGameOutputBoundary) {
        this.startGameDataAccessInterface = startGameDataAccessInterface;
        this.startGamePresenter = startGameOutputBoundary;
    }

    @Override
    public void execute(StartGameInputData startGameInputData) {
        final String team1Name = startGameInputData.getTeam1Name();
        final String team2Name = startGameInputData.getTeam2Name();

        // Check Valid Team Names
        if (!checkNotEmptyTeamNames(team1Name, team2Name)) {
            // prepare fail view
            startGamePresenter.prepareFailView("Team names must not be empty.");
        }
        // Check same name
        if (team1Name.equalsIgnoreCase(team2Name)) {
            startGamePresenter.prepareFailView("Team names must not be equal.");
        }

        // Check Team Already Exist in Database and Create new Team Objects
        if (startGameDataAccessInterface.teamExists(team1Name)) {
            Team team1 = startGameDataAccessInterface.getTeam(team1Name);
        }
        else {
            Team team1 = new Team(team1Name);
        }
        if (startGameDataAccessInterface.teamExists(team2Name)) {
            Team team2 = startGameDataAccessInterface.getTeam(team2Name);
        }
        else {
            Team team2 = new Team(team2Name);
        }
        // Create final output data
        // TODO: Implement output data
        final StartGameOutputData startGameOutputData = new StartGameOutputData();

        // Prepare Success View
        startGamePresenter.prepareSuccessView(startGameOutputData);

        return;
    }

    Boolean checkNotEmptyTeamNames(String team1Name, String team2Name) {
        // Check Empty
        if (!checkEmptyName(team1Name)) { return false; }
        if (!checkEmptyName(team2Name)) { return false; }


        return true;
    }

    Boolean checkEmptyName(String teamName) {
        return !"".equalsIgnoreCase(teamName);
    }
}
