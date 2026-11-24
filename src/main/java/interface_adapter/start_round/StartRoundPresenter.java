package interface_adapter.start_round;

import interface_adapter.game.GameState;
import interface_adapter.game.GameViewModel;
import use_case.start_round.StartRoundOutputBoundary;
import use_case.start_round.StartRoundOutputData;

public class StartRoundPresenter implements StartRoundOutputBoundary {
    private final GameViewModel viewModel;

    public StartRoundPresenter(GameViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(StartRoundOutputData data) {
        GameState state = new GameState();
        state.addImageURL(data.imageURL());

        state.setAttemptsLeft(data.attemptsLeft());
        state.setMessage("Guess the city!");

        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

}
