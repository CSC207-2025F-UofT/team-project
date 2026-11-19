package application_business_rules;

public interface SignUpInputBoundary {
  void execute(String userName, String password, String repeatPassword);
}
