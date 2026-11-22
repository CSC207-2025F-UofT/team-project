package app;

import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * Entry point of the application.
 */
public class Main {
  /**
   * Builds and shows the application. Requires the MASSIVE_API_KEY environment variable to be set.
   *
   * @param args Command line arguments (unused)
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      String apiKey = System.getenv("MASSIVE_API_KEY");
      if (apiKey == null || apiKey.isBlank()) {
        JOptionPane.showMessageDialog(null,
                "Please set the MASSIVE_API_KEY environment variable to your Massive.com API key.",
                "Missing API key", JOptionPane.ERROR_MESSAGE);
        return;
      }

      try {
        JFrame application = new AppBuilder(apiKey)
                .addSetRiskProfileView()
                .addSetRiskProfileUseCase()
                .build();
        application.setVisible(true);
      } catch (IOException e) {
        JOptionPane.showMessageDialog(null,
                "Could not create the configuration directory: " + e.getMessage(),
                "Startup error", JOptionPane.ERROR_MESSAGE);
      }
    });
  }
}
