package Game;

public class GamePresenter implements GameOutputBoundary {

    private final GameView view;

    public GamePresenter(GameView view) {
        this.view = view;
    }

    @Override
    public void prepareView(GameOutputData outputData) {

        GameViewModel viewModel = new GameViewModel(
                outputData.getRow(),
                outputData.getColumn(),
                outputData.getPlayer(),
                outputData.getWinner()
        );

        view.updateBoard(viewModel);
    }
}