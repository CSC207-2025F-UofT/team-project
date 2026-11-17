package entity.card;

public interface CardFactory {
    public Card create(String owner, String cardName);
}
