package use_case.add_card;

public interface AddCardInputBoundary {
    void execute(AddCardInputData InputData);
}

// AddCardInteractor should implement this
// AddCardController will depend on this interface