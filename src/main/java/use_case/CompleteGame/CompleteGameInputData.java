package use_case.CompleteGame;

public class CompleteGameInputData {
    // Empty because no input is needed.
    private final boolean isCompleted;

    public CompleteGameInputData(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    public boolean isCompleted() {
        return isCompleted;
    }
}
