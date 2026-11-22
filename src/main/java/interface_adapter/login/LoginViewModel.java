package interface_adapter.login;

public final class LoginViewModel {
    private int userProfileId;
    private String displayName = "";
    private String message = "";
    private boolean authenticated;
    private boolean error;

    public int getUserProfileId() { return userProfileId; }
    public String getDisplayName() { return displayName; }
    public String getMessage() { return message; }
    public boolean isAuthenticated() { return authenticated; }
    public boolean isError() { return error; }

    void setSuccess(int id, String name) {
        userProfileId = id; displayName = name;
        message = "Welcome, " + name + "."; authenticated = true; error = false;
    }
    void setFailure(String value) {
        message = value; authenticated = false; error = true;
    }
}
