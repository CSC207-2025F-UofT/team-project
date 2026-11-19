package use_case.flashcard_generator;

import data_access.FlashcardDataAccessInterface;
import entities.Card;
import entities.Deck;

import java.util.ArrayList;

public class FlashcardGeneratorInteractor implements FlashcardGeneratorInputBoundary {

    private final FlashcardDataAccessInterface DAO;
    private final FlashcardGeneratorOutputBoundary presenter;

    public FlashcardGeneratorInteractor(FlashcardDataAccessInterface dao, FlashcardGeneratorOutputBoundary presenter) {
        this.DAO = dao;
        this.presenter = presenter;
    }

    @Override
    public void execute(FlashcardGeneratorInputData data) {
        if (data.getListTextPiece().isEmpty()){
            presenter.prepareFailView();
            return;

        }
        else if (data.getListTextPiece().size() % 2 == 1){
            presenter.prepareFailView();
            return;

        }
        Deck deck = new Deck("Temp title", new ArrayList<>());
        for (int i=0; i < data.getListTextPiece().size()/2; i ++){

                FlashcardGeneratorInputData.TextPiece title = data.getTextPiece(2*i);
                FlashcardGeneratorInputData.TextPiece answer = data.getTextPiece(2*i + 1);

                Card flashcard = new Card(title.getText(), answer.getText());
                deck.addCard(flashcard);

        }
        DAO.saveDeck(deck);
        presenter.prepareSuccessView();
    }
}
