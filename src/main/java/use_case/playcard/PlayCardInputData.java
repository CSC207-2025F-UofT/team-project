package use_case.playcard;

import entity.card.CardColor;

public class PlayCardInputData {

    private final String playerID;
    private final int selectedCardIndex;
    private final CardColor chosenColor; // only for wild cards

    public PlayCardInputData(String playerID, int selectedCardIndex) {
        this.playerID = playerID;
        this.selectedCardIndex = selectedCardIndex;
        this.chosenColor = null;
    }

    public PlayCardInputData(String playerID, int selectedCardIndex, CardColor chosenColor) {
        this.playerID = playerID;
        this.selectedCardIndex = selectedCardIndex;
        this.chosenColor = chosenColor;
    }

    public String getPlayerID() {
        return playerID;
    }

    public int getSelectedCardIndex() {
        return selectedCardIndex;
    }

    public CardColor getChosenColor() {
        return chosenColor;
    }
}
