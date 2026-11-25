package view;

import javax.swing.JTextField;
import java.util.function.Consumer;

public final class NameScreen extends ArtworkMenu {
    public NameScreen(Consumer<String> play, Runnable back) {
        super("Name_Screen", 205, 1146, 1685, 77);
        JTextField nickname = addField("Nickname", 623, 855, 1284, 176);
        addButton("Nevermind", back, 149, 1251, 780, 210);
        addButton("Play", submitField(nickname, "Enter a nickname to continue.", play), 1445, 1251, 501, 232);
    }
}
