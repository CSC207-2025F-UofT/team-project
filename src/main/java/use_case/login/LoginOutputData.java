package use_case.login;

public class LoginOutputData {
    private final int userId;
    private final String userName;
    public LoginOutputData(int userId, String userName){
        this.userId = userId;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserId() {
        return userId;
    }
}
