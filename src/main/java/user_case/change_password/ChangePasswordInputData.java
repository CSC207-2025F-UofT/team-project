package user_case.change_password;

public class ChangePasswordInputData {
    private final String username;
    private final String password;

    public ChangePasswordInputData(String username, String password){
        this.username = username;
        this.password = password;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
}
