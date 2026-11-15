package use_case.create_study_set;

/**
 * The input boundary for the Create Study Set use case.
 */
public interface CreateStudySetInputBoundary {
  /**
   * Executes the use case.
   *
   * @param inputData the user id and requested name
   */
  void execute(CreateStudySetInputData inputData);
}