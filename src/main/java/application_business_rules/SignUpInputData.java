package application_business_rules;

public class SignUpInputData {
  private String userName;
  private String password;
  private String repeatPassword;

  public SignUpInputData(String userName, String password, String repeatPassword){
    this.userName = userName;
    this.password = password;
    this.repeatPassword = repeatPassword;
  }

  public String getUserName() {
    return userName;
  }

  public String getPassword() {
    return password;
  }

  public String getRepeatPassword() {
    return repeatPassword;
  }
}
