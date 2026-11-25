package entity.card;

import entity.Player;

public interface CardFactory {
    public Card create(Player owner, String cardName);
}
