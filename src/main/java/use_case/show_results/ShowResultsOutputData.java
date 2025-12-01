package use_case.show_results;

import java.util.List;

/**
 * Output data for showing results screen.
 */
public class ShowResultsOutputData {
    private final String userName;
    private final int totalMoves;
    private final List<String> pathHistory;
    private final String finalLocation;

    public ShowResultsOutputData(String userName, int totalMoves, List<String> pathHistory, String finalLocation) {
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
