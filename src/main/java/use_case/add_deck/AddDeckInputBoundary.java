package use_case.add_deck;

// implemented by AddDeckInteractor, used by AddDeckController
public interface AddDeckInputBoundary {
  void execute(AddDeckInputData inputData);
}
