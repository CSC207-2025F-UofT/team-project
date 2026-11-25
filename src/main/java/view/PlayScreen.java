package view;

public final class PlayScreen extends ArtworkMenu {
    public PlayScreen(Runnable host, Runnable join, Runnable load, Runnable back) {
        super("Play_Screen", 570, 1951, 1190, 86);
        addButton("Host Game", host, 800, 1171, 490, 183);
        addButton("Join Game", join, 792, 1453, 540, 158);
        addButton("Load Save", load, 789, 1749, 504, 163);
        addButton("Back", back, 119, 1771, 318, 152);
    }
}
