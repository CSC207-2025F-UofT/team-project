package use_case.flashcard_studying;
import data_access.FlashcardDataAccessInterface;
import entities.Deck;
import entities.Card;

public class FlashcardStudyingInteractor implements FlashcardStudyingInputBoundary {

    private final FlashcardStudyingOutputBoundary presenter;
    private final FlashcardDataAccessInterface dao;
    private Deck currentDeck;

    public FlashcardStudyingInteractor(FlashcardStudyingOutputBoundary presenter, FlashcardDataAccessInterface dao) {
        this.presenter = presenter;
        this.dao = dao;
    }

    public void execute(FlashcardStudyingInputData inputData) {
        // check if user inputted a new deck
        if (inputData.getDeckTitle() != null) {
            //check if in the DAO
            try {
                currentDeck = dao.loadDeck(inputData.getDeckTitle());
            } catch (IllegalStateException e) {
                presenter.prepareFailView("Invalid deck title");
            }
        } else if (currentDeck == null) {
            presenter.prepareFailView("You need to input a deck title");
        }
        // currentDeck is not null
        // execute buttonClicks then
        if (inputData.getButtonClicked().equals("Flip")) {
            Card currentCard = currentDeck.getCurrentCard();
            currentCard.flip();
            presenter.prepareSuccessView(new FlashcardStudyingOutputData(currentCard.getText()));
        } else if (inputData.getButtonClicked().equals("Still learning")) {
            Card currentCard = currentDeck.getCard();
            presenter.prepareSuccessView(new FlashcardStudyingOutputData(currentCard.getText()));
        } else if (inputData.getButtonClicked().equals("Knew it")) {
            currentDeck.removeCurrentCard();
            Card currentCard = currentDeck.getCard();
            presenter.prepareSuccessView(new FlashcardStudyingOutputData(currentCard.getText()));
        }

    }


}
