package interface_adapter.login;

public class LoginViewModel {
    private int id;
    private String userName;
    private String message;

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }
    public void setModel(int id, String userName, String message){
        this.id = id;
        this.userName = userName;
        this.message = message;
    }
    public boolean isSuccessful() {
        return id >= 0;
    }
}
