package interface_adapter.create_study_set;

import use_case.create_study_set.CreateStudySetInputBoundary;
import use_case.create_study_set.CreateStudySetInputData;

/**
 * The Controller for the Create Study Set use case.
 */
public class CreateStudySetController {
  private final CreateStudySetInputBoundary interactor;

  /**
   * Creates the controller.
   *
   * @param interactor the use case to call
   */
  public CreateStudySetController(CreateStudySetInputBoundary interactor) {
    this.interactor = interactor;
  }

  /**
   * Asks the use case to create a study set.
   *
   * @param userId the id of the logged-in user
   * @param name   the requested study set name, as typed
   */
  public void execute(String userId, String name) {
    interactor.execute(new CreateStudySetInputData(userId, name));
  }
}