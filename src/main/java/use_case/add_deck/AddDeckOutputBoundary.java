package use_case.add_deck;

// presenter should implement this
public interface AddDeckOutputBoundary {
  void prepareSuccessView(AddDeckOutputData outputData);

  void prepareFailureView(String error);
}
