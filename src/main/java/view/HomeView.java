package view;

import interface_adapters.cancel_timer.CancelTimerController;
import interface_adapters.home.HomeViewModel;
import interface_adapters.home.HomeViewModelListener;
import interface_adapters.open_settings.OpenSettingsController;
import interface_adapters.start_timer.StartTimerController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The View for when the user first enters the program.
 * Provides the initial focus timer interface.
 */
public class HomeView extends JPanel implements HomeViewModelListener {

    private HomeViewModel viewModel;
    private OpenSettingsController settingsController = null;
    private StartTimerController startTimerController = null;
    private CancelTimerController cancelTimerController = null;

    private JLabel timerLabel = new JLabel();

    private JButton startButton;
    private JButton adjustButton;
    private JButton cancelButton;

    public HomeView(HomeViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.addListener(this);

        // Main panel containing all sections of the home view.
        JPanel mainTimerPanel = new JPanel();
        mainTimerPanel.setLayout(new BoxLayout(mainTimerPanel, BoxLayout.Y_AXIS));
        mainTimerPanel.setBackground(Color.WHITE);

        // Top section containing the heading, timer display, and Start button.
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setPreferredSize(new Dimension(800, 220));
        topPanel.setBackground(Color.WHITE);

        JPanel middlePanel = new JPanel();
        middlePanel.setPreferredSize(new Dimension(800, 280));
        middlePanel.setBackground(Color.WHITE);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(800, 30));
        bottomPanel.setBackground(Color.WHITE);

        cancelButton = new JButton("Press to Cancel");
        cancelButton.setFont(FontManager.getAppleGaramond(24f));

        cancelButton.setBorderPainted(false);
        cancelButton.setContentAreaFilled(false);
        cancelButton.setFocusPainted(false);
        cancelButton.setVisible(false);

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelTimerController.execute();
            }
        });

        bottomPanel.add(cancelButton);

        // Panel reserved for navigation components.
        JPanel navigationPanel = new JPanel();
        navigationPanel.setPreferredSize(new Dimension(800, 30));
        navigationPanel.setBackground(Color.WHITE);

        JLabel focusTextLabel = new JLabel("Focus");
        focusTextLabel.setFont(FontManager.getAppleGaramond(24f));
        focusTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Panel containing the timer display and adjustment button.
        JPanel timerPanel = new JPanel();
        timerPanel.setMaximumSize(new Dimension(450, 120));
        timerPanel.setBackground(Color.WHITE);

        timerLabel = new JLabel("00:00");
        timerLabel.setFont(FontManager.getWinkle(100f));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Creates the button used to access timer adjustment settings.
        ImageIcon adjustIcon = new ImageIcon("src/images/AdjustIcon.png");
        adjustButton = new JButton(adjustIcon);
        adjustButton.setBorderPainted(false);
        adjustButton.setFocusPainted(false);
        adjustButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsController.execute();
            }
        });

        JPanel buttonWrapper = new JPanel(new BorderLayout());
        buttonWrapper.setBackground(Color.WHITE);
        buttonWrapper.setPreferredSize(new Dimension(90, 90));
        buttonWrapper.add(adjustButton, BorderLayout.CENTER);

        timerPanel.add(Box.createHorizontalStrut(90));
        timerPanel.add(timerLabel);
        timerPanel.add(buttonWrapper);

        startButton = new JButton("Start");
        startButton.setFont(FontManager.getWinkle(48f));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTimerController.execute("focus");
            }
        });


        topPanel.add(Box.createVerticalStrut(20));
        topPanel.add(focusTextLabel);
        topPanel.add(timerPanel);
        topPanel.add(startButton);
        topPanel.add(Box.createVerticalGlue());

        mainTimerPanel.add(topPanel);
        mainTimerPanel.add(middlePanel);
        mainTimerPanel.add(bottomPanel);
        mainTimerPanel.add(navigationPanel);

        this.setLayout(new BorderLayout());
        this.add(mainTimerPanel, BorderLayout.CENTER);

    }

    public void setStartTimerController(StartTimerController startTimerController) {
        this.startTimerController = startTimerController;
    }

    /**
     * Assign the settingsController to the Home View.
     * @param settingsController Open Settings Controller
     */
    public void setSettingsController(OpenSettingsController settingsController) {
        this.settingsController = settingsController;
    }

    public void setCancelTimerController(CancelTimerController cancelTimerController) {
        this.cancelTimerController = cancelTimerController;
    }

    /**
     * Updates the timer display when the Home View Model state changes.
     */
    @Override
    public void stateChanged() {
        String timerDisplay = viewModel.getState().getTimerDisplay();
        boolean isTimerRunning = viewModel.getState().isTimerRunning();

        timerLabel.setText(timerDisplay);

        // Enables start and adjust button when timer isn't running
        startButton.setVisible(!isTimerRunning);
        adjustButton.setVisible(!isTimerRunning);
        cancelButton.setVisible(isTimerRunning);
    }
}