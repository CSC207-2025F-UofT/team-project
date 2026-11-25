package interface_adapter.register;

public class RegisterState {
    private String username = "";
    private String password = "";
    private String email = "";
    private String errorMessage = "";

    public RegisterState(){}

    // Getters
    public String getEmail(){
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getErrorMessage(){
        return errorMessage;
    }

    // Setters
    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setErrorMessage(String errorMessage){
        this.errorMessage = errorMessage;
    }

    public void setEmail(String email){
        this.email = email;
    }
}
