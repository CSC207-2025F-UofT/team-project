package entity;

public class BlackjackGame {
    private final Deck deck;
    private final Hand playerHand;
    private final Hand dealerHand;
    private double wallet;
    private double bet;

    public BlackjackGame(double startingWallet) {
        this.deck = new Deck();
        this.playerHand = new Hand();
        this.dealerHand = new Hand();
        this.wallet = startingWallet;
    }

    public void startRound(double bet) {
        this.bet = bet;
        playerHand.clear();
        dealerHand.clear();
        deck.shuffle();
        playerHand.addCard(deck.draw());
        dealerHand.addCard(deck.draw());
        playerHand.addCard(deck.draw());
        dealerHand.addCard(deck.draw());
    }

    public void playerHit() { playerHand.addCard(deck.draw()); }

    public void dealerPlay() {
        while (dealerHand.getValue() < 17) {
            dealerHand.addCard(deck.draw());
        }
    }

    public GameResult determineResult() {
        if (playerHand.getValue() > 21) return GameResult.PLAYER_BUST;
        if (dealerHand.getValue() > 21) return GameResult.DEALER_BUST;
        if (playerHand.getValue() > dealerHand.getValue()) return GameResult.PLAYER_WIN;
        if (playerHand.getValue() < dealerHand.getValue()) return GameResult.DEALER_WIN;
        return GameResult.PUSH;
    }

    public void updateWallet(GameResult result) {
        switch (result) {
            case PLAYER_WIN:
                wallet += bet;
                break;
            case DEALER_WIN:
            case PLAYER_BUST:
                wallet -= bet;
                break;
            default:
                break;
        }
    }

    public Hand getPlayerHand() { return playerHand; }
    public Hand getDealerHand() { return dealerHand; }
    public double getWallet() { return wallet; }
}