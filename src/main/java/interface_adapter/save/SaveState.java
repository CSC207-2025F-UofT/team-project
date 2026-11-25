package interface_adapter.save;

import java.util.ArrayList;
import java.util.List;

public class SaveState {
    private String message = "";
    private boolean error = false;
    private List<String> leaderboardLines = new ArrayList<>();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<String> getLeaderboardLines() {
        return leaderboardLines;
    }

    public void setLeaderboardLines(List<String> leaderboardLines) {
        this.leaderboardLines = leaderboardLines;
    }
}
