package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * The View for when the user first enters the program.
 * Provides the initial focus timer interface.
 */
public class HomeView extends JPanel {

    private JButton adjustButton = new JButton();

    public HomeView() {

        Font winkle = null;
        try {
            winkle = Font.createFont(Font.TRUETYPE_FONT, new File("src/font/Winkle-Regular.ttf"));
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(winkle);

        Font appleGaramond = null;
        try {
            appleGaramond = Font.createFont(Font.TRUETYPE_FONT, new File("src/font/AppleGaramond.ttf"));
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ge.registerFont(appleGaramond);

        // Main panel containing all sections of the home view.
        JPanel mainTimerPanel = new JPanel();
        mainTimerPanel.setLayout(new BoxLayout(mainTimerPanel, BoxLayout.Y_AXIS));
        mainTimerPanel.setBackground(Color.WHITE);

        // Top section containing the heading, timer display, and Start button.
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setPreferredSize(new Dimension(800, 220));
        topPanel.setBackground(Color.WHITE);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(800, 310));
        bottomPanel.setBackground(Color.WHITE);

        // Panel reserved for navigation components.
        JPanel navigationPanel = new JPanel();
        navigationPanel.setPreferredSize(new Dimension(800, 30));
        navigationPanel.setBackground(Color.WHITE);

        JLabel focusTextLabel = new JLabel("Focus");
        focusTextLabel.setFont(appleGaramond.deriveFont(30f));
        focusTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel containing the timer display and adjustment button.
        JPanel timerPanel = new JPanel();
        timerPanel.setMaximumSize(new Dimension(450, 120));
        timerPanel.setBackground(Color.WHITE);

        JLabel timerLabel = new JLabel("00:00");
        timerLabel.setFont(winkle.deriveFont(100f));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Creates the button used to access timer adjustment settings.
        ImageIcon adjustIcon = new ImageIcon("src/images/AdjustIcon.png");
        adjustButton = new JButton(adjustIcon);
        adjustButton.setBorderPainted(false);
        adjustButton.setFocusPainted(false);

        timerPanel.add(Box.createHorizontalStrut(90));
        timerPanel.add(timerLabel);
        timerPanel.add(adjustButton);

        JButton startButton = new JButton("Start");
        startButton.setFont(winkle.deriveFont(48f));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        topPanel.add(Box.createVerticalStrut(20));
        topPanel.add(focusTextLabel);
        topPanel.add(timerPanel);
        topPanel.add(startButton);
        topPanel.add(Box.createVerticalGlue());

        mainTimerPanel.add(topPanel);
        mainTimerPanel.add(bottomPanel);
        mainTimerPanel.add(navigationPanel);

        this.setLayout(new BorderLayout());
        this.add(mainTimerPanel, BorderLayout.CENTER);

    }

    /**
     * Adds an ActionListener to the timer adjustment button.
     * @param listener the ActionListener that handles clicks on the button
     */
    public void addAdjustButtonListener(ActionListener listener) {
        adjustButton.addActionListener(listener);
    }

}