package entity;

public class UserSession {
    private String currentUsername;

    public void login(String username){
        this.currentUsername = username;
    }

    public void logout(){
        this.currentUsername = null;
    }

    public boolean isLoggedIn(){
        return currentUsername != null;
    }

    public String getCurrentUsername(){
        return currentUsername;
    }
}
