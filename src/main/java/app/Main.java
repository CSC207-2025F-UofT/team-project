package app;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public final class Main {
    private Main() { }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppBuilder app = new AppBuilder();
            JFrame frame = new JFrame("Escape Room Adventure");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override public void windowClosed(WindowEvent event) { app.close(); }
            });
            frame.setContentPane(app.view());
            frame.pack();
            frame.setLocationByPlatform(true);
            frame.setVisible(true);
            app.listStories();
        });
    }
}
