package interface_adapter.edit_card;

import use_case.edit_card.EditCardInputBoundary;
import use_case.edit_card.EditCardInputData;

public class EditCardController {
    private final EditCardInputBoundary interactor;

    public EditCardController(EditCardInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(int deckId, int cardId, String question, String answer) {
        interactor.execute(new EditCardInputData(deckId, cardId, question, answer));
    }
}
