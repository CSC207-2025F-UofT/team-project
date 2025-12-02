package service;

import interface_adapter.ViewPets.ViewPetsController;
import interface_adapter.update_profile.UpdateUserProfileController;
import interface_adapter.user_logout.UserLogoutController;
import view.MainDashboardView;
import view.profile.UserProfileView;

import javax.swing.*;

import entity.User;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.*;

import dataAccess.InMemoryPetDataAccessObject;
import dataAccess.BackendPetDataAccessObject;

import interface_adapter.pet_management.PetManagementController;
import interface_adapter.pet_management.PetManagementPresenter;
import interface_adapter.pet_management.PetManagementViewModel;

import use_case.pet_management.AddPetInputBoundary;
import use_case.pet_management.AddPetInteractor;
import use_case.pet_management.DeletePetInputBoundary;
import use_case.pet_management.DeletePetInteractor;
import use_case.pet_management.PetDataAccessInterface;
import use_case.pet_management.PetManagementOutputBoundary;
import use_case.pet_management.UpdatePetInputBoundary;
import use_case.pet_management.UpdatePetInteractor;

import view.AdminPetManagementView;

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

    // ===== Admin: Pet Management windows =====
    /**
     *  Open the pet management screen from "Add Pet" menu item.
     *  */
    public void showAddPetPage() {
        showPetManagementDialog("Add Pet", AdminPetManagementView.Mode.ADD);
    }

    /**
     * Open the  pet management screen from "Modify Pet".
     * */
    public void showModifyPetPage() {
        showPetManagementDialog("Modify Pet", AdminPetManagementView.Mode.MODIFY);
    }

    /**
     * Open the same pet management screen from "Delete Pet".
     * */
    public void showDeletePetPage() {
        showPetManagementDialog("Delete Pet", AdminPetManagementView.Mode.DELETE);
    }

    /**
     * Common helper: show the AdminPetManagementView in a modal dialog.
     * */
    private void showPetManagementDialog(String title, AdminPetManagementView.Mode mode) {
        JPanel panel = buildPetManagementPanel(mode);

        JDialog dialog = new JDialog(this, title, true); // modal dialog
        dialog.setContentPane(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }


    /** Builds AdminPetManagementView wired up to the Clean Architecture stack. */
    private JPanel buildPetManagementPanel(AdminPetManagementView.Mode mode) {
        // ViewModel
        PetManagementViewModel vm = new PetManagementViewModel();

        // DAO implementing PetDataAccessInterface
        PetDataAccessInterface petGateway = new BackendPetDataAccessObject(backend);

        // Presenter & use cases
        PetManagementOutputBoundary presenter = new PetManagementPresenter(vm);

        AddPetInputBoundary addUC = new AddPetInteractor(petGateway, presenter);
        UpdatePetInputBoundary updateUC = new UpdatePetInteractor(petGateway, presenter);
        DeletePetInputBoundary deleteUC = new DeletePetInteractor(petGateway, presenter);

        // Controller
        PetManagementController controller =
                new PetManagementController(addUC, updateUC, deleteUC);

        // View in the right mode
        AdminPetManagementView view = new AdminPetManagementView(vm, mode);
        view.setController(controller);

        return view;
    }

}
