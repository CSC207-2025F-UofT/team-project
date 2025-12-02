package view;

import interface_adapter.homepage.HomepageState;
import interface_adapter.homepage.HomepageViewModel;
import interface_adapter.user_logout.UserLogoutController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class HomepageView extends JPanel implements ActionListener, PropertyChangeListener {

    // update start 20251124
    private final String viewName = "homepage";
    // update end 20251124
    private final HomepageViewModel homepageViewModel;

    private final JLabel username;

    private final JButton petpending;
    private final JButton homepage;
    private UserLogoutController userLogoutController;

    public HomepageView(HomepageViewModel homepageViewModel) {
        this.homepageViewModel = homepageViewModel;
        this.homepageViewModel.addPropertyChangeListener(this);
        this.username = new JLabel(homepageViewModel.getState().getUsername());

        setLayout(new BorderLayout());

        // ===== Top Header Bar =====
        final JPanel topBar = new JPanel();
        final JLabel titleLabel = new JLabel("Homepage");
        topBar.add(titleLabel);

        // ===== middle =====
        final JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setAlignmentX(Component.CENTER_ALIGNMENT);

        // === “Hello, ” + username ===
        final JPanel helloRow = new JPanel();
        helloRow.setLayout(new BoxLayout(helloRow, BoxLayout.X_AXIS));

        final JLabel helloLabel = new JLabel("Hello, ");
        final JLabel usernameLabel = this.username;

        helloRow.add(helloLabel);
        helloRow.add(usernameLabel);
        helloRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JButton logoutButton = new JButton("Logout");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(Box.createVerticalStrut(20));
        center.add(helloRow);
        center.add(Box.createVerticalGlue());
        center.add(logoutButton);
        center.add(Box.createVerticalStrut(20));

        logoutButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(logoutButton)) {
                        this.userLogoutController.execute();
                    }
                }
        );
        
        // ===== Botton Navigation Bar =====
        final JPanel bottomNav = new JPanel();
        bottomNav.setLayout(new GridLayout(1, 2));

        petpending = new JButton("Pet");
        homepage = new JButton("Homepage");

        bottomNav.add(petpending);
        bottomNav.add(homepage);

        // ===== add to main page =====
        this.add(topBar, BorderLayout.NORTH);
        this.add(center, BorderLayout.CENTER);
        this.add(bottomNav, BorderLayout.SOUTH);
    }

    public String getViewName() {
        return viewName;
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }
    
    public void setUserLogoutController(UserLogoutController userLogoutController) {
        this.userLogoutController = userLogoutController;
    }
    
    // add start 20251124
    // ===== update username and title =====
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final HomepageState state = (HomepageState) evt.getNewValue();
            this.username.setText(state.getUsername());
            final JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                frame.setTitle("Homepage - " + homepageViewModel.getState().getUsername());
            }            
        }

    }

}
