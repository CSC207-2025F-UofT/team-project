package use_case.flashcard_generator;

import data_access.FlashcardDataAccessInterface;
import entities.Card;
import entities.Deck;

import java.util.ArrayList;

/**
 * Implements the flashcard generation use case by converting text into Flashcard entities in a Flashcard Deck entity
 * and then saves it into a new JSON file through the DataAccessInterface
 */
public class FlashcardGeneratorInteractor implements FlashcardGeneratorInputBoundary {

    private final FlashcardDataAccessInterface DAO;
    private final FlashcardGeneratorOutputBoundary presenter;

    public FlashcardGeneratorInteractor(FlashcardDataAccessInterface dao, FlashcardGeneratorOutputBoundary presenter) {
        this.DAO = dao;
        this.presenter = presenter;
    }

    /**
     * Creates Card and Deck entities based off text user inputted, then saves it to a new JSON file and preparing the
     * success and fail views if data was not inputted correctly.
     * @param data the InputData object
     */
    @Override
    public void execute(FlashcardGeneratorInputData data) {
        if (data.getListTextPiece() == null || data.getListTextPiece().isEmpty()){ // No text is entered
            presenter.prepareFailView("You have to enter at least one question and answer");
            return;

        }
        else if (data.getListTextPiece().size() % 2 == 1){ // Not perfectly paired, user needs to add an answer
            presenter.prepareFailView("Incorrect format (missing answer)");
            return;

        }

        for (FlashcardGeneratorInputData.TextPiece piece: data.getListTextPiece()) { //Ensure that there's no empty space
            if (piece == null || piece.getText() == null || piece.getText().isBlank()) {
                presenter.prepareFailView("Incorrect format (empty space)");
                return;
            }

        }
        Deck deck = new Deck("Temp title", new ArrayList<>());
        for (int i=0; i < data.getListTextPiece().size()/2; i ++){ // Creates the deck

                FlashcardGeneratorInputData.TextPiece title = data.getTextPiece(2*i);
                FlashcardGeneratorInputData.TextPiece answer = data.getTextPiece(2*i + 1);

                if (!(title.isBold()) || answer.isBold()) { // makes sure its a bold/unbold pair
                    presenter.prepareFailView("Incorrect format (bolding error)");
                    return;
                }
            Card flashcard = new Card(title.getText(), answer.getText());
                deck.addCard(flashcard);

        }
        DAO.saveDeck(deck);
        FlashcardGeneratorOutputData output = new FlashcardGeneratorOutputData(deck.getTitle(), deck);
        presenter.prepareSuccessView(output);
    }
}
