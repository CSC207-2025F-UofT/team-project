package interface_adapter.Loggedin;

import java.util.ArrayList;
import java.util.List;

import entities.CognitiveData;

/**
 * The state for the Logged-in View.
 */
public class LoggedinState {
    private String username = "";
    private String role = "";
    private List<CognitiveData> entries = new ArrayList<>();
    private boolean dataLoaded = false;
    private String dataError = "";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<CognitiveData> getEntries() {
        return entries;
    }

    public void setEntries(List<CognitiveData> entries) {
        this.entries = entries;
    }

    public boolean isDataLoaded() {
        return dataLoaded;
    }

    public void setDataLoaded(boolean dataLoaded) {
        this.dataLoaded = dataLoaded;
    }

    public String getDataError() {
        return dataError;
    }

    public void setDataError(String dataError) {
        this.dataError = dataError;
    }
}
