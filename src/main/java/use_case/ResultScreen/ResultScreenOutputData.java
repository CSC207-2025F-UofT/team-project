package use_case.ResultScreen;

import java.util.List;

public class ResultScreenOutputData {
    private String userName;
    private int totalMoves;
    private List<String> pathHistory;
    private String finalLocation;

    public void ShowResultsOutputData(String userName, int totalMoves, List<String> pathHistory, String finalLocation) {
        this.userName = userName;
        this.totalMoves = totalMoves;
        this.pathHistory = pathHistory;
        this.finalLocation = finalLocation;
    }

    public ResultScreenOutputData(String userName, int totalMoves, List<String> pathHistory, String finalLocation) {
        this.userName = userName;
        this.totalMoves = totalMoves;
        this.pathHistory = pathHistory;
        this.finalLocation = finalLocation;
    }

    public String getUserName() {
        return userName;
    }

    public int getTotalMoves() {
        return totalMoves;
    }

    public List<String> getPathHistory() {
        return pathHistory;
    }

    public String getFinalLocation() {
        return finalLocation;
    }
}
