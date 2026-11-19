package view;

import interface_adapters.home.HomeViewModel;
import interface_adapters.home.HomeViewModelListener;
import interface_adapters.open_settings.OpenSettingsController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * The View for when the user first enters the program.
 * Provides the initial focus timer interface.
 */
public class HomeView extends JPanel implements HomeViewModelListener {

    private HomeViewModel viewModel;
    private OpenSettingsController controller = null;
    JLabel timerLabel = new JLabel();

    public HomeView(HomeViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.addListener(this);

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

        timerLabel = new JLabel("00:00");
        timerLabel.setFont(winkle.deriveFont(100f));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Creates the button used to access timer adjustment settings.
        ImageIcon adjustIcon = new ImageIcon("src/images/AdjustIcon.png");
        JButton adjustButton = new JButton(adjustIcon);
        adjustButton.setBorderPainted(false);
        adjustButton.setFocusPainted(false);
        adjustButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.execute();
            }
        });

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
     * Assign the controller to the Home View.
     * @param controller Open Settings Controller
     */
    public void setController(OpenSettingsController controller) {
        this.controller = controller;
    }

    /**
     * Updates the timer display when the Home View Model state changes.
     */
    @Override
    public void stateChanged() {
        timerLabel.setText(viewModel.getState().getTimerDisplay());
    }
}