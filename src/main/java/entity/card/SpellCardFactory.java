package entity.card;

public class SpellCardFactory implements CardFactory {
    @Override
    public SpellCard create(String owner, String cardName) {
        return switch (cardName) {
            case "Magic Missiles" -> new SpellCardBuilder(owner, cardName)
                    .damage(50)
                    .range(4)
                    .build();
            case "Fireball" -> new SpellCardBuilder(owner, cardName)
                    .damage(100)
                    .range(3)
                    .areaOfEffect(2)
                    .build();
            //TODO: Add remaining spell cards

            default -> throw new IllegalArgumentException(
                    "Unknown Spell Card: " + cardName
            );
        };
    }
}
