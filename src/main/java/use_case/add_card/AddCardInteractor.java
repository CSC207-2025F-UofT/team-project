package use_case.add_card;

import entity.Card;

public class AddCardInteractor implements AddCardInputBoundary {
    private final AddCardDataAccessInterface dataAccess;
    private final AddCardOutputBoundary presenter;

    public AddCardInteractor(AddCardDataAccessInterface dataAccess, AddCardOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    // check the deck exists first
    @Override
    public void execute(AddCardInputData inputData) {
        if (!dataAccess.deckExists(inputData.getDeckId())) {
            presenter.prepareFailView("Deck does not exist.");
            return;
        }

        // create the new card
        Card card;
        try {
            card = new Card(
                dataAccess.generateCardId(),
                inputData.getQuestion(),
                inputData.getAnswer()
            );
        } catch (IllegalArgumentException exception) {
            presenter.prepareFailView(exception.getMessage());
            return;
        }

        // save the card through data access interface 
        dataAccess.saveCard(inputData.getDeckId(), card);

        // send our result to output boundary
        AddCardOutputData outputData = new AddCardOutputData(
            card.getId(),
            inputData.getDeckId(),
            card.getQuestion(),
            card.getAnswer()
        );

        // notify presenter of okay
        presenter.prepareSuccessView(outputData);
    }
}