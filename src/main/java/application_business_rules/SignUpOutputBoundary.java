package application_business_rules;

public interface SignUpOutputBoundary {
  void prepareFailureView(String error);
  void prepareSuccessView(SignUpOutputData signUpOutputData);

}