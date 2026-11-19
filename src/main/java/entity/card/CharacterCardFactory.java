package entity.card;

import entity.Player;

public class CharacterCardFactory implements CardFactory {
    @Override
    public CharacterCard create(Player owner, String cardName) {
        return switch (cardName) {
            case "Aki", "Kia" -> new CharacterCard(owner, cardName, 200, 3);
            case "Cadence" -> new CharacterCard(owner, cardName, 250, 2);
            case "Flaire" -> new CharacterCard(owner, cardName, 200, 2);
            case "Cel" -> new CharacterCard(owner, cardName, 150, 4);
            //TODO: Add remaining character cards

            default -> throw new IllegalArgumentException(
                    "Unknown Character Card: " + cardName
            );
        };
    }
}
