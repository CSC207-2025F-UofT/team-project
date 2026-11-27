package use_case.playcard;

import entity.Game;
import entity.Player;
import entity.card.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayCardInteractor implements PlayCardInputBoundary{

    private final PlayCardDataAccessInterface gameDataAccessObject;
    private final PlayCardOutputBoundary playCardPresenter;

    public PlayCardInteractor(PlayCardDataAccessInterface gameDataAccessObject,
                              PlayCardOutputBoundary playCardOutputBoundary) {
        this.gameDataAccessObject = gameDataAccessObject;
        this.playCardPresenter = playCardOutputBoundary;
    }


    @Override
    public void execute(PlayCardInputData playCardInputData) {
        String playerId = playCardInputData.getPlayerID();  // perhaps we don't need this???
        int selectedCardIndex = playCardInputData.getSelectedCardIndex();
        CardColor chosenColor = playCardInputData.getChosenColor();

        Game game = gameDataAccessObject.getCurrentGame();
        Player currentPlayer = game.getCurrentPlayer();
        List<Card> hand = currentPlayer.getHand();
        Card selectedCard = hand.get(selectedCardIndex);


        if (game == null) {
            playCardPresenter.prepareFailView("No active game.");
            return;
        } else if (!currentPlayer.getId().equals(playerId)) {
            playCardPresenter.prepareFailView("It is not this player's turn.");
            return;
        } else if (!game.canPlayCard(selectedCard)) {
            playCardPresenter.prepareFailView("That card cannot be played.");
            return;
        } else {  // player succesfully plays the card
            // player discards their card from hand
            final Card discardedCard = currentPlayer.playCard(selectedCardIndex);
            game.addToDiscardPile(discardedCard); // goes to discard pile

            // output data arguments
            final String nexplayerID = game.getNextPlayer().getId();
            String effectMessage = null;

            // Applies card affect to game if any. Currently uses a bunch of different if statements.
            // TODO: This should be replaced by some better design latter... Use CardEffect Interface Instead.
            if (discardedCard instanceof SkipCard) {
                game.skipNextPlayer();
                effectMessage = "Skipped ____ ";
            } else if (discardedCard instanceof ReverseCard) {
                game.reverseDirection();
                effectMessage = "Reverse direction";
            }
            if (discardedCard instanceof DrawTwoCard) {
                game.drawCardsForNextPlayer(2);
                effectMessage = "____ has to draw two cards";
            }
            if (discardedCard instanceof WildDrawFourCard) {
                game.drawCardsForNextPlayer(4);
                game.changeColor(chosenColor);
                effectMessage = "____ chose the Color ____, and ____ has to draw four";
            }
            if (discardedCard instanceof WildCard) {
                game.changeColor(chosenColor);
                effectMessage = "_____ chose the Color ____";
            }

            game.checkForWinner();  // update for winner

            final List<Card> updatedHandCopy = new ArrayList<>(currentPlayer.getHand());
            final boolean gameFinished = game.isFinished();
            final String winnerID = game.getWinnerId();

            gameDataAccessObject.saveGame(game);

            final PlayCardOutputData playCardOutputData = new PlayCardOutputData(discardedCard,
                    updatedHandCopy, nexplayerID, game.getCurrentColor(), effectMessage,
                    gameFinished, winnerID);

            playCardPresenter.prepareSuccessView(playCardOutputData);
        }
    }
}
