package interface_adapter.game;

import use_case.update_game.UpdateGameOutputBoundary;
import use_case.update_game.UpdateGameOutputData;

/** Turns a tick's result into a new GameState for the view. */
public class GamePresenter implements UpdateGameOutputBoundary {
    private final GameViewModel viewModel;

    public GamePresenter(GameViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(UpdateGameOutputData outputData) {
        viewModel.setState(new GameState(
                (int) Math.round(outputData.getPlayerX()),
                (int) Math.round(outputData.getPlayerY()),
                outputData.getPlayerSize()));
    }
}
