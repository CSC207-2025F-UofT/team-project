package entity;

public class UserPreferences {
    private final String theme;
    private final String notification;

    public UserPreferences() {
        this.theme = "light";
        this.notification = "email";
    }
}
