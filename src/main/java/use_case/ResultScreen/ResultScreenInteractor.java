package use_case.ResultScreen;

public class ResultScreenInteractor implements ResultScreenInputBoundary {

    private final ResultScreenOutputBoundary presenter;

    public ResultScreenInteractor(ResultScreenOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void execute(ResultScreenInputData inputData) {

        if (!inputData.isGameCompleted()) {
            presenter.prepareSuccessView(
                    new ResultScreenOutputData("Error: Game is not completed")
            );
            return;
        }

        ResultScreenOutputData output =
                new ResultScreenOutputData("Game Completed!");

        presenter.prepareSuccessView(output);
    }
}
