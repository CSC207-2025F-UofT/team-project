package view.settings;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ExecutionException;
import javax.swing.*;

public final class SettingsMenu {

    private SettingsMenu() {
    }

    public static void install(JFrame window) {
        MusicPlayer player = new MusicPlayer();

        JMenuBar menuBar = window.getJMenuBar();

        if (menuBar == null) {
            menuBar = new JMenuBar();
            window.setJMenuBar(menuBar);
        }

        JMenu settings = new JMenu("Settings");

        JCheckBoxMenuItem darkMode = new JCheckBoxMenuItem(
                "Dark mode", ThemeManager.isDarkMode());

        darkMode.addActionListener(event ->
                ThemeManager.setDarkMode(darkMode.isSelected()));

        JCheckBoxMenuItem music =
                new JCheckBoxMenuItem("Background music", false);

        music.addActionListener(event -> {
            boolean shouldPlay = music.isSelected();
            music.setEnabled(false);

            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    if (shouldPlay) {
                        player.play();
                    } else {
                        player.stop();
                    }
                    return null;
                }

                @Override
                protected void done() {
                    music.setEnabled(true);

                    try {
                        get();
                    } catch (InterruptedException exception) {
                        Thread.currentThread().interrupt();
                        music.setSelected(false);
                    } catch (ExecutionException exception) {
                        music.setSelected(false);

                        if (window.isDisplayable()) {
                            JOptionPane.showMessageDialog(
                                    window,
                                    "Could not play music. Check the audio "
                                            + "file and your sound output.",
                                    "Music error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }.execute();
        });

        settings.add(darkMode);
        settings.add(music);
        menuBar.add(settings);

        window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                player.close();
            }

            @Override
            public void windowClosed(WindowEvent event) {
                player.close();
            }
        });
    }
}