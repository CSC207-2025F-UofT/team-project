package interface_adapter.earn_reward_coins;

import use_case.earn_reward_coins.EarnRewardCoinsInputBoundary;
import use_case.earn_reward_coins.EarnRewardCoinsInputData;
import use_case.earn_reward_coins.EarnRewardCoinsStatusInputBoundary;

public class EarnRewardCoinsController {
    private final EarnRewardCoinsInputBoundary interactor;
    private final EarnRewardCoinsStatusInputBoundary statusInteractor;

    public EarnRewardCoinsController(
            EarnRewardCoinsInputBoundary interactor,
            EarnRewardCoinsStatusInputBoundary statusInteractor) {
        this.interactor = interactor;
        this.statusInteractor = statusInteractor;
    }

    public void execute(String userId) {
        interactor.execute(new EarnRewardCoinsInputData(userId));
    }

    public void loadStatus(String userId) {
        statusInteractor.execute(userId);
    }
}