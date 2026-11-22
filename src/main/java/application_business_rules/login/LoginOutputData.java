package application_business_rules.login;

public final class LoginOutputData {
    private final int userProfileId;
    private final String displayName;

    public LoginOutputData(int userProfileId, String displayName) {
        this.userProfileId = userProfileId;
        this.displayName = displayName;
    }
    public int getUserProfileId() { return userProfileId; }
    public String getDisplayName() { return displayName; }
}
