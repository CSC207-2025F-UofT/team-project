package user_case.change_password;

public class ChangePasswordOutputData {
    private final String username;

    public ChangePasswordOutputData(String username){
        this.username = username;
    }
    public String getUsername(){
        return username;
    }
}
