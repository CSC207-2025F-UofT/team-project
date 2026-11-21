
import main.view.SoundManager;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        PokerBuilder pokerBuilder = new PokerBuilder();
        JFrame application = pokerBuilder
                .addMainMenuView()
                .addGameView()
                .addSettingsView()
                .build();
        
        application.setLocationRelativeTo(null);
        application.setVisible(true);
        application.setResizable(false);

        SoundManager soundManager = new SoundManager();
        soundManager.playLoop("/Balatro_Main_Theme.wav");
    }
}
