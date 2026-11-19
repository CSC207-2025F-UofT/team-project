package interface_adapter;

import application_business_rules.SignUpInputBoundary;
import application_business_rules.SignUpInputData;

public class SignUpController {
  private final SignUpInputBoundary signUpInteractor;

  public SignUpController(SignUpInputBoundary signUpInteractor){
    this.signUpInteractor = signUpInteractor;
  }

  public void execute(String userName, String password, String repeatPassword){
    SignUpInputData inputData = new SignUpInputData(userName, password, repeatPassword);
    signUpInteractor.execute(inputData.getUserName(), inputData.getPassword(), inputData.getRepeatPassword());
  }
}
