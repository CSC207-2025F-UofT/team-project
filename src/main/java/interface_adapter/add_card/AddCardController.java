package interface_adapter.add_card;

import use_case.add_card.AddCardInputBoundary;
import use_case.add_card.AddCardInputData;

public class AddCardController {
    private final AddCardInputBoundary interactor;

    public AddCardController(AddCardInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(int deckId, String question, String answer) {
        AddCardInputData inputData = new AddCardInputData(deckId, question, answer);
        
        interactor.execute(inputData);
    }
}