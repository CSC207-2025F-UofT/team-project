package use_case.ResultScreen;

public class ResultScreenInputData {
    private final boolean gameCompleted;

    public ResultScreenInputData(boolean gameCompleted) {
        this.gameCompleted = gameCompleted;
    }

    public boolean isGameCompleted() {
        return gameCompleted;
    }
}
