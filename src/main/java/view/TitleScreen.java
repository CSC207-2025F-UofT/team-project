package view;

public final class TitleScreen extends ArtworkMenu {
    public TitleScreen(Runnable start, Runnable rules, Runnable quit) {
        super("Title_Screen", 274, 1948, 1494, 89);
        addButton("Start", start, 805, 1183, 535, 176);
        addButton("Rules", rules, 768, 1466, 560, 169);
        addButton("Quit", quit, 814, 1754, 575, 165);
    }
}
