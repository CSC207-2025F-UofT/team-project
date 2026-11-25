package view;

import adapters.viewmodel.LobbyViewModel;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.util.List;

public final class WaitingToStartScreen extends ArtworkMenu {
    private final List<JLabel> playerRows = List.of(addText(526, 783, 1043, 177), addText(526, 974, 1043, 163));
    private final JButton start;

    public WaitingToStartScreen(Runnable exit, Runnable startGame) {
        super("Wait_Start", 260, 429, 1447, 125);
        addButton("Exit", exit, 202, 1353, 360, 213);
        start = addButton("Start", startGame, 1337, 1331, 548, 299);
        start.setEnabled(false);
    }

    void render(LobbyViewModel lobby) {
        for (int index = 0; index < playerRows.size(); index++) {
            String player = index < lobby.players().size() ? lobby.players().get(index) : "";
            playerRows.get(index).setText(player);
        }
        start.setEnabled(lobby.canStart());
    }
}
