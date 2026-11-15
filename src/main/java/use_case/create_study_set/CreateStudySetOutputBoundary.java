package use_case.create_study_set;

/**
 * The output boundary for the Create Study Set use case.
 */
public interface CreateStudySetOutputBoundary {
  /**
   * Prepares the success view for the use case.
   *
   * @param outputData the id and name of the new study set
   */
  void prepareSuccessView(CreateStudySetOutputData outputData);

  /**
   * Prepares the fail view for the use case.
   *
   * @param errorMessage a message explaining the problem, suitable for the user
   */
  void prepareFailView(String errorMessage);
}