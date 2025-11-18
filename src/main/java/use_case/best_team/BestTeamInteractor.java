package use_case.best_team;

import entity.BestTeamOptimizer;
import entity.BestTeamResult;
import entity.Player;
import java.util.List;

public class BestTeamInteractor implements BestTeamInputBoundary {
    private final BestTeamDataAccessInterface bestTeamDataAccess;
    private final BestTeamOutputBoundary bestTeamPresenter;

    public BestTeamInteractor(BestTeamDataAccessInterface bestTeamDataAccess, BestTeamOutputBoundary bestTeamPresenter) {
        this.bestTeamDataAccess = bestTeamDataAccess;
        this.bestTeamPresenter = bestTeamPresenter;
    }

    @Override
    public void execute(BestTeamRequestModel bestTeamRequestModel) {
        List<Player> players = bestTeamDataAccess.getAllPlayers();

        BestTeamOptimizer optimizer = new BestTeamOptimizer(bestTeamRequestModel.getBudget());
        BestTeamResult result = optimizer.computeBestTeam(players);

        BestTeamResponseModel response = new BestTeamResponseModel(result.getPlayers(), result.getTotalCost(), result.getTotalPredictedPoints());

        bestTeamPresenter.present(response);
    }
}
