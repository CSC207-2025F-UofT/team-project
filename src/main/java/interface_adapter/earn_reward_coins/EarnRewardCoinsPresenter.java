package interface_adapter.earn_reward_coins;
import use_case.earn_reward_coins.EarnRewardCoinsInputData;
import use_case.earn_reward_coins.EarnRewardCoinsOutputBoundary;
import use_case.earn_reward_coins.EarnRewardCoinsOutputData;
import use_case.earn_reward_coins.EarnRewardCoinsStatusOutputBoundary;

public class EarnRewardCoinsPresenter
        implements EarnRewardCoinsOutputBoundary,
        EarnRewardCoinsStatusOutputBoundary{
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
        state.setEligible(false);
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

    @Override
    public void presentStatus(
            int balance,
            boolean eligible,
            boolean alreadyClaimed,
            String message) {

        EarnRewardCoinsState state = viewModel.getState();

        state.setCoinBalance(balance);
        state.setEligible(eligible);
        state.setRewardClaimed(alreadyClaimed);
        state.setMessage(message);
        state.setError("");

        viewModel.firePropertyChanged();
    }
}
