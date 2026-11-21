
import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        PokerBuilder pokerBuilder = new PokerBuilder();
        JFrame application = pokerBuilder
                .addMainMenuView()
                .addGameView()
                .build();
        
        application.setLocationRelativeTo(null);
        application.setVisible(true);
        application.setResizable(false);
    }
}
