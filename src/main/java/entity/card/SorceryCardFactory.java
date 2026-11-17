package entity.card;

public class SorceryCardFactory implements CardFactory {
    @Override
    public SorceryCard create(String owner, String cardName) {
        return switch (cardName) {
            case "Mirror", "Portal" -> new SorceryCard(owner, cardName, true);

            //TODO: Add Remaining Sorcery Cards

            default -> throw new IllegalArgumentException(
                    "Unknown Character Card: " + cardName
            );
        };
    }
}
