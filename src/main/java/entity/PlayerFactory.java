package entity;

import entity.card.Card;

import java.util.List;

/**
 * Factory for creating common player objects
 */
public class PlayerFactory {

    public Player create(String id, List<Card> hand) {
        return new Player(id, hand);
    }
}
