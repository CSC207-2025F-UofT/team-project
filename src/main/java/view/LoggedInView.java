package view;

import interface_adapter.logged_in.ChangePasswordController;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.logout.LogoutController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * The View for when the user is logged into the program.
 * It acts as the container for the full Dashboard interface.
 */
public class LoggedInView extends JPanel implements ActionListener, PropertyChangeListener {

    public final String viewName = "logged in";
    private final LoggedInViewModel loggedInViewModel;

    private DashboardView dashboard = null;
    private JFrame applicationFrame = null;

    private ChangePasswordController changePasswordController = null;
    private LogoutController logoutController = null;

    // Modified Constructor (No longer takes JFrame)
    public LoggedInView(LoggedInViewModel loggedInViewModel) {
        this.loggedInViewModel = loggedInViewModel;
        this.loggedInViewModel.addPropertyChangeListener(this);

        // Set a temporary placeholder until setApplicationFrame is called
        this.setLayout(new BorderLayout());
        this.add(new JLabel("Loading Dashboard...", SwingConstants.CENTER), BorderLayout.CENTER);
    }

    // NEW Setter Method - Called by AppBuilder.build()
    /**
     * Injects the main application frame and finalizes the view setup
     * by creating and displaying the DashboardView.
     */
    public void setApplicationFrame(JFrame applicationFrame) {
        if (this.dashboard != null) {
            return;
        }

        this.applicationFrame = applicationFrame;

        // CRITICAL STEP: Now we have the JFrame, we can instantiate the DashboardView
        this.dashboard = new DashboardView(this.applicationFrame);

        // Load the dashboard into this panel
        this.removeAll();
        this.add(dashboard, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();

        // Pass any controllers that were set before the dashboard was created
        if (changePasswordController != null) {
            dashboard.setChangePasswordController(changePasswordController);
        }
        if (logoutController != null) {
            dashboard.setLogoutController(logoutController);
        }

        // Send the current username to the dashboard to update the welcome message
        propertyChange(new PropertyChangeEvent(this, "state", null, loggedInViewModel.getState()));

        // =================================================================
        // ✅ NEW: Maximizing the Window After Login is Complete
        // =================================================================
        if (this.applicationFrame != null) {
            // Set the main application JFrame to the maximized (full screen) state
            this.applicationFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            // Ensure the frame is visible (if it wasn't already) and update layout
            this.applicationFrame.setVisible(true);
            this.applicationFrame.revalidate();
        }
    }

    /**
     * React to a button click. All action logic is now inside DashboardView.
     */
    public void actionPerformed(ActionEvent evt) {
        // This is now mostly a placeholder since the buttons are in DashboardView
        System.out.println("Click from LoggedInView container: " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final LoggedInState state = (LoggedInState) evt.getNewValue();

            // Pass the username to the method on the dashboard
            if (dashboard != null) {
                dashboard.updateUsername(state.getUsername());
            }
        }
        // All password change/error popups should now be initiated by components inside DashboardView
        // and its associated controllers/presenters.
    }

    public String getViewName() {
        return viewName;
    }

    // Modified controller setters to handle the delayed Dashboard initialization
    public void setChangePasswordController(ChangePasswordController changePasswordController) {
        this.changePasswordController = changePasswordController;
        if (dashboard != null) {
            dashboard.setChangePasswordController(changePasswordController);
        }
    }

    public void setLogoutController(LogoutController logoutController) {
        this.logoutController = logoutController;
        if (dashboard != null) {
            dashboard.setLogoutController(logoutController);
        }
    }
}