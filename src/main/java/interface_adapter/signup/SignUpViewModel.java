package interface_adapter.signup;
public final class SignUpViewModel {
    private String message="";
    private boolean success;
    public String getMessage(){return message;}
    public boolean isSuccess(){return success;}
    public void setMessage(String message,boolean success){this.message=message;this.success=success;}
}
