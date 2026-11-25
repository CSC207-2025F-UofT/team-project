package view;

import javax.swing.JTextField;
import java.util.function.Consumer;

public final class JoinScreen extends ArtworkMenu {
    private final JTextField code = addField("Lobby code", 623, 858, 1284, 177);

    public JoinScreen(Consumer<String> join, Runnable back) {
        super("Join_Screen", 263, 1052, 1525, 116);
        addButton("Back", back, 155, 1292, 468, 161);
        addButton("Enter", submitField(code, "Enter a lobby code.", join), 1453, 1245, 401, 188);
    }

    void clearCode() {
        code.setText("");
    }
}
