package view;

import interface_adapters.menu_bar.NavigationBarViewModel;
import interface_adapters.menu_bar.NavigationBarViewModelListener;
import interface_adapters.menu_bar.SwitchViewController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The navigation bar panel that allows users to switch between different views of the application.
 */
public class NavigationBarPanel extends JPanel implements NavigationBarViewModelListener {

    private final NavigationBarViewModel navigationBarViewModel;
    private SwitchViewController switchViewController = null;

    private final JLabel pageTitleLabel;
    private JMenuItem gameMenuItem;
    private JMenuItem focusMenuItem;
    private JMenuItem makeFlashcardsMenuItem;
    private JMenuItem studyFlashcardsMenuItem;

    public NavigationBarPanel(NavigationBarViewModel navigationBarViewModel) {
        this.navigationBarViewModel = navigationBarViewModel;
        this.navigationBarViewModel.addListener(this);

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setPreferredSize(new Dimension(800, 44));
        this.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        this.setBackground(Color.WHITE);

        // Create the label displaying the title of the current page.
        pageTitleLabel = new JLabel("Focus"); // Starts off on the Focus Home Page
        pageTitleLabel.setFont(FontManager.getAppleGaramond(24f));
        pageTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Build the popup menu containing the available navigation options.
        JPopupMenu navPopup = buildNavigationPopup();

        // Create and configure the menu button.
        ImageIcon menuIcon = new ImageIcon("src/images/menuIcon.png");
        JButton menuButton = new JButton(menuIcon);
        menuButton.setBorderPainted(false);
        menuButton.setContentAreaFilled(false);
        menuButton.setFocusPainted(false);

        // Display the navigation popup when the menu button is clicked.
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navPopup.show(menuButton, 0, menuButton.getHeight() + 5);
            }
        });

        // Add the components to the navigation bar.
        this.add(menuButton);
        this.add(Box.createHorizontalGlue());
        this.add(pageTitleLabel);
        this.add(Box.createHorizontalGlue());
        this.add(Box.createHorizontalStrut(menuButton.getPreferredSize().width));

    }

    /**
     * Assigns the switchViewController to the Navigation Bar Panel.
     * @param switchViewController Cancel Timer Controller
     */
    public void setSwitchViewController(SwitchViewController switchViewController) {
        this.switchViewController = switchViewController;
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

        // Create a menu item for each application view.
        focusMenuItem = buildMenuItem("Focus", ViewNames.HOME);
        makeFlashcardsMenuItem = buildMenuItem("Make Flashcards", ViewNames.MAKE_FLASHCARDS);
        studyFlashcardsMenuItem = buildMenuItem("Study Flashcards", ViewNames.STUDY_FLASHCARDS);
        gameMenuItem = buildMenuItem("Game", ViewNames.GAME);

        // Add the menu items to the popup menu.
        popup.add(focusMenuItem);
        popup.add(makeFlashcardsMenuItem);
        popup.add(studyFlashcardsMenuItem);
        popup.add(gameMenuItem);

        return popup;
    }

    /**
     * Creates a styled menu item for the navigation popup.
     * @param title the text displayed for the menu item
     * @return a JMenuItem representing a page of the app
     */
    private JMenuItem buildMenuItem(String title, String viewName) {
        JMenuItem item = new JMenuItem(title);
        item.setFont(FontManager.getGinny(30f));
        item.setBackground(Color.WHITE);
        item.setOpaque(true);
        item.setBorder(BorderFactory.createEmptyBorder(0, 10, 8, 40));

        // Switch to the selected view when the menu item is clicked.
        item.addActionListener(e -> {
            if (switchViewController != null) {
                switchViewController.execute(viewName);
            }
        });
        return item;
    }

    /**
     * Updates the navigation bar when the Home View Model state changes.
     */
    @Override
    public void stateChanged() {
        String currentView = navigationBarViewModel.getState().getCurrentViewName();
        boolean isTimerRunning = navigationBarViewModel.getState().isTimerRunning();

        // Update the page title based on the currently displayed view.
        switch(currentView) {
            case ViewNames.HOME:
                pageTitleLabel.setText("Focus");
                break;
            case ViewNames.MAKE_FLASHCARDS:
                pageTitleLabel.setText("Make Flashcards");
                break;
            case ViewNames.STUDY_FLASHCARDS:
                pageTitleLabel.setText("Study Flashcards");
                break;
            case ViewNames.GAME:
                pageTitleLabel.setText("Game");
                break;
            default:
                pageTitleLabel.setText("");
        }

        // Hide the menu item for the current view.
        focusMenuItem.setVisible(!ViewNames.HOME.equals(currentView));
        makeFlashcardsMenuItem.setVisible(!ViewNames.MAKE_FLASHCARDS.equals(currentView));
        studyFlashcardsMenuItem.setVisible(!ViewNames.STUDY_FLASHCARDS.equals(currentView));

        // Hide the Game option while the timer is running or when already on the Game view.
        gameMenuItem.setVisible(!isTimerRunning && !ViewNames.GAME.equals(currentView));
    }

}
