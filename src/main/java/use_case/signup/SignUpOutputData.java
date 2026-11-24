package use_case.signup;
public final class SignUpOutputData {
    private final int userId;
    private final String name;
    private final String message;
    public SignUpOutputData(int userId,String name,String message){this.userId=userId;this.name=name;this.message=message;}
    public int getUserId(){return userId;}
    public String getName(){return name;}
    public String getMessage(){return message;}
}
