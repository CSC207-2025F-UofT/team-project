
import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        PokerBuilder pokerBuilder = new PokerBuilder();
        JFrame application = pokerBuilder.build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
