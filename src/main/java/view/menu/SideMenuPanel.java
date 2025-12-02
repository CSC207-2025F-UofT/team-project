package view.menu;

import javax.swing.*;

import interface_adapter.FilterPets.PetListViewModel;
import interface_adapter.ViewPets.ViewPetsController;

import java.awt.*;
import java.util.Map;
import service.Backend;
import service.Frontend;
import view.AdminPage;
import view.MainDashboardView;
import view.pets.PetListView;
import view.profile.EditProfileView;
import view.profile.UserProfileView;

public class SideMenuPanel extends JPanel {

    private final Frontend frontend;
    private final Backend backend;
    private final MainDashboardView dashboard; 
    private String userType;

    public SideMenuPanel(Frontend frontend, Backend backend, MainDashboardView dashboard, String userType) {
        this.frontend = frontend;
        this.backend = backend;
        this.dashboard = dashboard;
        this.userType = userType;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(0xF5F7FB));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // show title
        JLabel title = new JLabel("Hello, " + capitalize(userType));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title);
        add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Load Menu Configuration
        Map<String, String[]> config = MenuConfig.getMenuItems();
        String[] items = config.getOrDefault(userType, new String[]{"Log Out"});

        // Automatically generate buttons based on configuration
        for (String item : items) {
            add(createMenuButton(item));
            add(Box.createRigidArea(new Dimension(0, 10)));
        }
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(160, 40));

        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        btn.setFocusPainted(false);

        btn.addActionListener(e -> handleMenuClick(text));

        return btn;
    }

    private void handleMenuClick(String item) {
        switch (item) {
	        case "My Profile":
	        	dashboard.setContent(new UserProfileView(backend));
	            break;
	        case "Edit Profile":
	        	dashboard.setContent(new EditProfileView(backend, frontend.getUpdateProfileController()));
	            break;
            case "My Applications":
//              frontend.showAddPetPage();
              break;
            case "Manage Applications":
                dashboard.setContent(new AdminPage(frontend, backend));
                break;
            case "View Pets": 
                ViewPetsController c = frontend.getViewPetsController();

                if (c != null) {
                    c.onViewPets();
                    dashboard.setContent(new PetListView(c.getViewModel(), c));
                }
                break;
            case "Call API":
                backend.getPetsFromApi();
                break;
            case "Add Pet":
                frontend.showAddPetPage();
                break;
            case "Modify Pet":
                frontend.showModifyPetPage();
                break;
            case "Delete Pet":
                frontend.showDeletePetPage();
                break;
            case "Log Out":
                frontend.logout();
                break;
        }
    }

    private String capitalize(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}
