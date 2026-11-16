package interface_adapter.earn_reward_coins;
import use_case.earn_reward_coins.EarnRewardCoinsInputBoundary;
import use_case.earn_reward_coins.EarnRewardCoinsInputData;
import use_case.earn_reward_coins.EarnRewardCoinsInteractor;

public class EarnRewardCoinsController {
    private final EarnRewardCoinsInputBoundary interactor;

    public EarnRewardCoinsController(EarnRewardCoinsInteractor interactor){
        this.interactor = interactor;
    }

    public void execute(String userId){
        EarnRewardCoinsInputData inputData =
                new EarnRewardCoinsInputData((userId));
        interactor.execute(inputData);
    }
}
