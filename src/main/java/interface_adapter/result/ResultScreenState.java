package interface_adapter.result;

import java.util.List;

public class ResultScreenState {
    private String userName = "";
    private int totalMoves = 0;
    private List<String> pathHistory;
    private List<String> locationsVisited;
    private String finalLocation = "";

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTotalMoves() {
        return totalMoves;
    }

    public void setTotalMoves(int totalMoves) {
        this.totalMoves = totalMoves;
    }

    public List<String> getPathHistory() {
        return pathHistory;
    }

    public void setPathHistory(List<String> pathHistory) {
        this.pathHistory = pathHistory;
    }

    public List<String> getLocationsVisited() {
        return locationsVisited;
    }

    public void setLocationsVisited(List<String> locationsVisited) {
        this.locationsVisited = locationsVisited;
    }

    public String getFinalLocation() {
        return finalLocation;
    }

    public void setFinalLocation(String finalLocation) {
        this.finalLocation = finalLocation;
    }

}
