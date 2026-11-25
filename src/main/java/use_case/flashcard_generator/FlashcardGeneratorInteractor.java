package use_case.flashcard_generator;

import data_access.FlashcardDataAccessInterface;
import entities.Card;
import entities.Deck;
import use_case.flashcard_generator.FlashcardGeneratorInputData;

import java.util.ArrayList;
import java.util.List;

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
     *
     * @param data the InputData object
     */
    @Override
    public void execute(FlashcardGeneratorInputData data) {
        List<FlashcardGeneratorInputData.TextPiece> formattedTextPieces = combineTextPieces(data);
        if (formattedTextPieces == null || formattedTextPieces.isEmpty()) { // No text is entered
            presenter.prepareFailView("You have to enter at least one question and answer");
            return;

        }

        for (FlashcardGeneratorInputData.TextPiece piece : formattedTextPieces) { //Ensure that there's no empty space
            if (piece == null || piece.getText() == null || piece.getText().isBlank()) {
                presenter.prepareFailView("Incorrect format (empty space)");
                return;
            }
            if (formattedTextPieces.size() % 2 == 1){
                presenter.prepareFailView("Incorrect format, please see tutorial");
                return;
            }

        }
        Deck deck = new Deck("Temp title", new ArrayList<>());
        for (int i = 0; i < formattedTextPieces.size() / 2; i++) { // Creates the deck

            FlashcardGeneratorInputData.TextPiece title = formattedTextPieces.get(2 * i);
            FlashcardGeneratorInputData.TextPiece answer = formattedTextPieces.get(2 * i + 1);

            if (!(title.isBold()) || answer.isBold()) { // makes sure it's a bold/unbold pair
                presenter.prepareFailView("Incorrect format, please see tutorial");
                return;
            }
            Card flashcard = new Card(title.getText(), answer.getText());
            deck.addCard(flashcard);

        }
        DAO.saveDeck(deck);
        FlashcardGeneratorOutputData output = new FlashcardGeneratorOutputData(deck.getTitle(), deck);
        presenter.prepareSuccessView(output);
    }

    /**
     * Combines all the questions and answers into condensed TextPieces
     * @param data
     * @return the list of formatted TextPieces
     */
    private List<FlashcardGeneratorInputData.TextPiece> combineTextPieces(FlashcardGeneratorInputData data) {
        List<FlashcardGeneratorInputData.TextPiece> textPieces = data.getListTextPiece();
        List<FlashcardGeneratorInputData.TextPiece> formattedTextPieces = new ArrayList<>();
        Boolean currentBold = null;
        FlashcardGeneratorInputData.TextPiece pieceToAdd = null;
        for (FlashcardGeneratorInputData.TextPiece piece : textPieces) {
            if (piece.getText().isBlank()){
                if(pieceToAdd != null){
                    pieceToAdd.setText(pieceToAdd.getText() + piece.getText());
                }
                continue; // ends current iteration if white space occurs
            }
            if (currentBold == null) {
                currentBold = piece.isBold();
                pieceToAdd = new FlashcardGeneratorInputData.TextPiece(piece.getText(), piece.isBold());
            }
            else if (currentBold == piece.isBold()) {
                pieceToAdd.setText(pieceToAdd.getText() + " " + piece.getText());
            } else {
                currentBold = piece.isBold();
                formattedTextPieces.add(pieceToAdd);
                pieceToAdd = new FlashcardGeneratorInputData.TextPiece(piece.getText(), piece.isBold());
            }


        }
        if (pieceToAdd != null) {
            formattedTextPieces.add(pieceToAdd);
        }
        return formattedTextPieces;
    }
}
