package interface_adapter.earn_reward_coins;
import use_case.earn_reward_coins.EarnRewardCoinsInputData;
import use_case.earn_reward_coins.EarnRewardCoinsOutputBoundary;
import use_case.earn_reward_coins.EarnRewardCoinsOutputData;

public class EarnRewardCoinsPresenter
        implements EarnRewardCoinsOutputBoundary{
    private final EarnRewardCoinsViewModel viewModel;

    public EarnRewardCoinsPresenter(EarnRewardCoinsViewModel viewModel){
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(EarnRewardCoinsOutputData outputData){
        EarnRewardCoinsState state = viewModel.getState();
        state.setCoinBalance(outputData.getNewBalance());
        state.setMessage(
                "You earned " + outputData.getCoinsEarned() + " coins!"
        );
        state.setError("");
        state.setRewardClaimed(true);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage){
        EarnRewardCoinsState state = viewModel.getState();
        state.setMessage("");
        state.setError(errorMessage);
        viewModel.firePropertyChanged();
    }

}
