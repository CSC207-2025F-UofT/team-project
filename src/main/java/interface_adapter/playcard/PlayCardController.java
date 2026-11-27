package interface_adapter.playcard;

import entity.card.CardColor;
import use_case.playcard.PlayCardInputBoundary;
import use_case.playcard.PlayCardInputData;

public class PlayCardController {

    private final PlayCardInputBoundary playCardUseCaseInteractor;

    public PlayCardController(PlayCardInputBoundary playCardUseCaseInteractor) {
        this.playCardUseCaseInteractor = playCardUseCaseInteractor;
    }

    public void execute(String playerID, int selectedCardIndex, CardColor chosenColor) {
        final PlayCardInputData playCardInputData = new PlayCardInputData(playerID, selectedCardIndex, chosenColor);

        playCardUseCaseInteractor.execute(playCardInputData);

    }
}
