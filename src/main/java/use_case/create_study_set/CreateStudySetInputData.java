package use_case.create_study_set;

/**
 * The data the Create Study Set use case needs to run.
 */
public class CreateStudySetInputData {
  private final String userId;
  private final String name;

  /**
   * Creates input data.
   *
   * @param userId the id of the logged-in user
   * @param name   the requested study set name, as typed
   */
  public CreateStudySetInputData(String userId, String name) {
    this.userId = userId;
    this.name = name;
  }

  /**
   * Returns the user id.
   *
   * @return the id of the logged-in user
   */
  public String getUserId() { return userId; }

  /**
   * Returns the requested name.
   *
   * @return the study set name, as typed
   */
  public String getName() { return name; }
}