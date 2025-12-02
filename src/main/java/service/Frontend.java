package service;

import interface_adapter.ViewPets.ViewPetsController;
import interface_adapter.update_profile.UpdateUserProfileController;
import interface_adapter.user_logout.UserLogoutController;
import view.EditProfileView;
import view.MainDashboardView;
import view.PetListView;
import view.SideMenuPanel;
import view.UserProfileView;

import javax.swing.*;

import entity.User;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.*;

public class Frontend extends JFrame {

    private final Backend backend;

    // The controller that handles logout use case from Clean Architecture.
    private UserLogoutController userLogoutController;

    // The card panel containing all login/signup views from the AppBuilder.
    private JPanel cardPanel;
	private CardLayout cardLayout;

    private UpdateUserProfileController updateProfileController;
    private ViewPetsController viewPetsController;
    private MainDashboardView currentDashboard;
    
    /**
     * Constructs the application frontend window.
     *
     * @param backend the backend instance used for saving/loading data
     */
    public Frontend(Backend backend) {
        this.backend = backend;
        final int frontendWidth = 600;
        final int frontendHeight = 400;

        setTitle("Pet Adoption System");
        setSize(frontendWidth, frontendHeight);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                backend.toJsonFiles("users.json", "pets.json", "applications.json");
                dispose();
                System.exit(0);
            }
        });
    }

    /**
     * Registers the card panel (containing Login/Signup views) created by AppBuilder.
     * This becomes the initial UI shown when the application starts.
     *
     * @param cardPanel the shared panel containing all card-based views
     * @param cardLayout 
     */
    public void setCardPanel(JPanel cardPanel, CardLayout cardLayout) {
        this.cardPanel = cardPanel;        
        this.cardLayout = cardLayout;
        setContentPane(cardPanel);
    }

    //---------------------------by YZ----------------------------------
    /**
     * Stores the logout controller so that the dashboard or navbar can call logout().
     *
     * @param controller the logout use case controller
     */
    public void setUserLogoutController(UserLogoutController controller) {
        this.userLogoutController = controller;
    }

    /**
     * Shows the dashboard for a specific user type.
     * Dashboard views are not part of the cardPanel — they are standalone JPanel
     * instances. This method replaces the main window content with the dashboard.
     *
     * @param userType the type of the logged-in user (admin/staff/user)
     */
    public void showDashboard(String userType) {
    	currentDashboard = new MainDashboardView(this, backend, userType);
        setContentPane(currentDashboard);
        revalidate();
        repaint();
    }

    public void setUpdateProfileController(UpdateUserProfileController controller) {
        this.updateProfileController = controller;
    }
    
    public UpdateUserProfileController getUpdateProfileController() {
        return updateProfileController;
    }
 
    public void showMyProfile() {
    	currentDashboard.setContent(new UserProfileView(backend));
    }
   
    public void setViewPetsController(ViewPetsController controller) {
        this.viewPetsController = controller;
    }
   
	public ViewPetsController getViewPetsController() {
		return viewPetsController;
	}
    
    /**
     * Logs the user out using Clean Architecture logic, then returns the UI
     * back to the original cardPanel containing login/signup screens.
     */
    public void logout() {
        if (userLogoutController != null) {
            userLogoutController.execute();
        }

        // restart cardPanel（login/signup）
        if (cardPanel != null) {
            setContentPane(cardPanel); 
            revalidate();
            repaint();
        }
    }


}
