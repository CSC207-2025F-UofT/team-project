package interface_adapter.welcome;

import use_case.welcome.WelcomeInputBoundary;

/**
 * Controller for the Welcome Use Case.
 */
public class WelcomeController {

  private final WelcomeInputBoundary userWelcomeUseCaseInteractor;

  public WelcomeController(WelcomeInputBoundary userWelcomeUseCaseInteractor) {
    this.userWelcomeUseCaseInteractor = userWelcomeUseCaseInteractor;
  }
  /**
   * TODO: Execute the Welcome Use Case and switch to another view.
   */
}
