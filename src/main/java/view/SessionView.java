package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class SessionView extends JPanel {

    public SessionView() {
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

        JPanel mainTimerPanel = new JPanel();
        mainTimerPanel.setLayout(new BoxLayout(mainTimerPanel, BoxLayout.Y_AXIS));
        mainTimerPanel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setPreferredSize(new Dimension(800, 200));
        topPanel.setBackground(Color.WHITE);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(800, 300));
        bottomPanel.setBackground(Color.WHITE);

        JPanel navigationPanel = new JPanel();
        navigationPanel.setPreferredSize(new Dimension(800, 30));
        navigationPanel.setBackground(Color.WHITE);

        JLabel focusTextLabel = new JLabel("Focus");
        focusTextLabel.setFont(appleGaramond.deriveFont(30f));
        focusTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel timerPanel = new JPanel();
        timerPanel.setMaximumSize(new Dimension(450, 120));
        timerPanel.setBackground(Color.WHITE);

        JLabel timerLabel = new JLabel("00:00");
        timerLabel.setFont(winkle.deriveFont(100f));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        ImageIcon adjustIcon = new ImageIcon("src/images/AdjustIcon.png");
        JButton adjustTimer = new JButton(adjustIcon);
        adjustTimer.setBorderPainted(false);
        adjustTimer.setFocusPainted(false);

        adjustTimer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open the settings panel
            }
        });

        timerPanel.add(Box.createHorizontalStrut(90));
        timerPanel.add(timerLabel);
        timerPanel.add(adjustTimer);

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

        JFrame mainFrame = new JFrame();
        mainFrame.add(mainTimerPanel);
        mainFrame.setSize(800, 600);
        mainFrame.setVisible(true);
        mainFrame.setLocationRelativeTo(null);

    }

    public static void main(String[] args){
        SessionView v = new SessionView();
    }
}