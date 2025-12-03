package interface_adapter.homescreen;

public class HomeScreenState {
    private String username = "";
    private String errorMessage = null;

    public HomeScreenState() {}

    public HomeScreenState(HomeScreenState copy) {
        this.username = copy.username;
        this.errorMessage = copy.errorMessage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}