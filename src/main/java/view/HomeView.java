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

    private JButton menuButton;
    private JPopupMenu navPopup;
    private JMenuItem gameMenuItem;

    public HomeView(HomeViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.addListener(this); // Register this view as a listener of the view model.

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
        middlePanel.setPreferredSize(new Dimension(800, 260));
        middlePanel.setBackground(Color.WHITE);

        // Bottom panel of the screen which stores the button to cancel the timer.
        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(800, 40));
        bottomPanel.setBackground(Color.WHITE);

        cancelButton = new JButton("Press to Cancel");
        cancelButton.setAlignmentX(SwingConstants.CENTER);
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

        // The panel that contains the menu bar.
        JPanel navigationPanel = new JPanel();
        navigationPanel.setLayout(new BoxLayout(navigationPanel, BoxLayout.X_AXIS));
        navigationPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 24));
        navigationPanel.setPreferredSize(new Dimension(800, 24));
        navigationPanel.setBackground(Color.WHITE);

        JLabel focusTextLabel = new JLabel("Focus");
        focusTextLabel.setFont(FontManager.getAppleGaramond(24f));
        focusTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Builds the menu bar popup.
        navPopup = buildNavigationPopup();

        ImageIcon menuIcon = new ImageIcon("src/images/menuIcon.png");
        menuButton = new JButton(menuIcon);
        menuButton.setBorderPainted(false);
        menuButton.setContentAreaFilled(false);
        menuButton.setFocusPainted(false);

        // Displays the menu when clicked
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navPopup.show(menuButton, 0, menuButton.getHeight() + 5);
            }
        });

        navigationPanel.add(menuButton);
        navigationPanel.add(Box.createHorizontalGlue());
        navigationPanel.add(focusTextLabel);
        navigationPanel.add(Box.createHorizontalGlue());

        int buttonWidth = menuButton.getPreferredSize().width;
        navigationPanel.add(Box.createHorizontalStrut(buttonWidth));

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

        // Wraps the adjust timer button.
        JPanel buttonWrapper = new JPanel(new BorderLayout());
        buttonWrapper.setBackground(Color.WHITE);
        buttonWrapper.setPreferredSize(new Dimension(90, 90));
        buttonWrapper.add(adjustButton, BorderLayout.CENTER);

        timerPanel.add(Box.createHorizontalStrut(90));
        timerPanel.add(timerLabel);
        timerPanel.add(buttonWrapper);

        // Start button for the timer.
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
        topPanel.add(navigationPanel);
        topPanel.add(timerPanel);
        topPanel.add(startButton);
        topPanel.add(Box.createVerticalGlue());

        mainTimerPanel.add(topPanel);
        mainTimerPanel.add(middlePanel);
        mainTimerPanel.add(bottomPanel);

        this.setLayout(new BorderLayout());
        this.add(mainTimerPanel, BorderLayout.CENTER);

    }

    /**
     * Assigns the startTimerController to the Home View.
     * @param startTimerController Start Timer Controller
     */
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

    /**
     * Assigns the cancelTimerController to the Home View.
     * @param cancelTimerController Cancel Timer Controller
     */
    public void setCancelTimerController(CancelTimerController cancelTimerController) {
        this.cancelTimerController = cancelTimerController;
    }

    /**
     * Builds the navigation popup menu that appears when the user clicks the menu bar.
     * @return a JPopupMenu that contains the pages of the app.
     */
    private JPopupMenu buildNavigationPopup() {
        JPopupMenu popup = new JPopupMenu();
        popup.setBackground(Color.WHITE);
        popup.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        String[] pages = {"Make Flashcards", "Study Flashcards"};
        for (String page : pages) {
            popup.add(this.buildMenuItem(page));
        }

        gameMenuItem = buildMenuItem("Game");
        popup.add(gameMenuItem);

        return popup;
    }

    /**
     * Creates a styled menu item for the navigation popup.
     * @param title the text displayed for the menu item
     * @return a JMenuItem representing a page of the app
     */
    private JMenuItem buildMenuItem(String title) {
        JMenuItem item = new JMenuItem(title);
        item.setFont(FontManager.getGinny(30f));
        item.setBackground(Color.WHITE);
        item.setOpaque(true);
        item.setBorder(BorderFactory.createEmptyBorder(0, 10, 8, 40));

        item.addActionListener(e -> {
            // TODO: Add call to controller
            System.out.println("clicked: " + title);
        });
        return item;
    }

    /**
     * Updates the timer display when the Home View Model state changes.
     */
    @Override
    public void stateChanged() {
        String timerDisplay = viewModel.getState().getTimerDisplay();
        boolean isTimerRunning = viewModel.getState().isTimerRunning();

        timerLabel.setText(timerDisplay);

        // Enables start, adjust button, and the game menu item when timer isn't running
        // Enables the cancel button when the timer is running
        startButton.setVisible(!isTimerRunning);
        adjustButton.setVisible(!isTimerRunning);
        cancelButton.setVisible(isTimerRunning);
        gameMenuItem.setVisible(!isTimerRunning);
    }
}