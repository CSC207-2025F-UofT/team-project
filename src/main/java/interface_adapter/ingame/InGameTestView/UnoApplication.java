package interface_adapter.ingame.InGameTestView;

import javax.swing.*;

/** Starts the standalone visual-only UNO table demo. */
public final class UnoApplication {
    private UnoApplication() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            setSystemLookAndFeel();
            JFrame window = new JFrame("UNO — In-Game View Demo");
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            InGameTestView gameView = new InGameTestView();
            gameView.updateGameState(GameTestViewState.demo());
            window.setContentPane(gameView);
            window.setSize(1080, 610);
            window.setMinimumSize(new java.awt.Dimension(820, 580));
            window.setLocationByPlatform(true);
            window.setVisible(true);
        });
    }

    private static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // The default Swing look and feel remains usable.
        }
    }
}
