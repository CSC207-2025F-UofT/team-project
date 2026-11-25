package view;

import javax.swing.JTextField;

public final class HostScreen extends ArtworkMenu {
    private final JTextField code = addField("Hosted lobby code", 576, 587, 1132, 136);

    public HostScreen(Runnable exit) {
        super("Host_Screen", 260, 429, 1447, 125);
        code.setEditable(false);
        addButton("Exit", exit, 205, 1353, 360, 210);
    }

    void showCode(String roomCode) {
        code.setText(roomCode);
    }
}
