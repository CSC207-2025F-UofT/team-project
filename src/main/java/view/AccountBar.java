package view;

import java.awt.FlowLayout;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The strip at the top of the gallery. A visitor who has not signed in sees a Log in button; an adopter who has
 * signed in sees their name and the personal pages: My profile, My applications and Sign out.
 */
public class AccountBar extends JPanel {
    private final JLabel greeting = new JLabel();
    private final JButton logIn = new JButton("Log in");
    private final JButton profile = new JButton("My profile");
    private final JButton applications = new JButton("My applications");
    private final JButton signOut = new JButton("Sign out");

    /**
     * Creates the bar. It starts in the signed-out state.
     *
     * @param onLogIn opens the login screen
     * @param onProfile opens My profile
     * @param onApplications opens My applications
     * @param onSignOut signs the adopter out
     */
    public AccountBar(Runnable onLogIn, Runnable onProfile, Runnable onApplications, Runnable onSignOut) {
        super(new FlowLayout(FlowLayout.LEFT));
        getAccessibleContext().setAccessibleName("Account");
        greeting.putClientProperty("html.disable", true);
        greeting.getAccessibleContext().setAccessibleName("Signed in user");
        setUp(logIn, "Log in", KeyEvent.VK_L, onLogIn);
        setUp(profile, "My profile", KeyEvent.VK_Y, onProfile);
        setUp(applications, "My applications", KeyEvent.VK_A, onApplications);
        setUp(signOut, "Sign out", KeyEvent.VK_O, onSignOut);
        add(greeting);
        add(logIn);
        add(profile);
        add(applications);
        add(signOut);
        showGuest();
    }

    private void setUp(JButton button, String name, int mnemonic, Runnable action) {
        button.setMnemonic(mnemonic);
        button.getAccessibleContext().setAccessibleName(name);
        button.addActionListener(event -> action.run());
    }

    /**
     * Shows what a visitor who has not signed in sees.
     */
    public void showGuest() {
        greeting.setText("Browsing as a guest");
        logIn.setVisible(true);
        profile.setVisible(false);
        applications.setVisible(false);
        signOut.setVisible(false);
    }

    /**
     * Shows what a signed-in adopter sees.
     *
     * @param name the adopter's name
     */
    public void showSignedIn(String name) {
        greeting.setText("Signed in as " + name);
        logIn.setVisible(false);
        profile.setVisible(true);
        applications.setVisible(true);
        signOut.setVisible(true);
    }
}
