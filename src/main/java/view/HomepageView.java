package view;

import entity.Pet;
import interface_adapter.homepage.HomepageState;
import interface_adapter.homepage.HomepageViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
//Angelica's imports for linking the "Pet" button on the homepage to the list of pets

import api.APIInterface;
import data_access.PetDataAccessInterface;
import view.PetListView;
import interface_adapter.controllers4ViewFilterPets.ViewPetsController;
import interface_adapter.presenters4ViewFilterPets.ViewPetsPresenter;
import interface_adapter.ViewModel4ViewFilterPets.PetListViewModel;
import use_case.view_pets.ViewPetsInputBoundary;
import use_case.view_pets.ViewPetsInteractor;
import use_case.view_pets.ViewPetsOutputBoundary;

//public class HomepageView extends JPanel {
public class HomepageView extends JPanel implements ActionListener, PropertyChangeListener {

//	update start 20251124
//    private final String viewName = "Homepage";
    private final String viewName = "homepage";
//	update end 20251124
    private final HomepageViewModel homepageViewModel;

    private final JLabel username;

    private final JButton petpending;
    private final JButton homepage;


//    public HomepageView(HomepageViewModel homepageViewModel, JLabel username){
//    public HomepageView(HomepageViewModel homepageViewModel){
//        this.homepageViewModel = homepageViewModel;
////        this.username = new JLabel(homepageViewModel.getViewName());
//        this.username = new JLabel("Hello, " + homepageViewModel.getState().getUsername());
////         this.petPendingViewModel.addPropertyChangeListener(this);
//        this.homepageViewModel.addPropertyChangeListener(this);
//
////        final JLabel titleLabel = new JLabel("Home Page");
////        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
//
//        final JLabel title = this.username;
//        title.setAlignmentX(Component.CENTER_ALIGNMENT);
//        
//        final JPanel petpending_button = new JPanel();
//        petpending = new JButton("Pet");
//        petpending_button.add(petpending);
//
//        final JPanel homepage_button = new JPanel();
//        homepage = new JButton("Homepage");
//        homepage_button.add(homepage);
//
//        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
//
////        this.add(titleLabel);
//        this.add(title);
//        this.add(username);
//        this.add(petpending_button);
//        this.add(homepage_button);
//    }

    public HomepageView(HomepageViewModel homepageViewModel){
        this.homepageViewModel = homepageViewModel;
        this.homepageViewModel.addPropertyChangeListener(this);
        this.username = new JLabel(homepageViewModel.getState().getUsername());  // 动态部分

        setLayout(new BorderLayout());

        // ===== 顶部标题栏 =====
        JPanel topBar = new JPanel();
        JLabel titleLabel = new JLabel("Homepage");
        topBar.add(titleLabel);

        // ===== 中间内容区 =====
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setAlignmentX(Component.CENTER_ALIGNMENT);

        // === “Hello, ” + username 放在同一行 ===
        JPanel helloRow = new JPanel();
        helloRow.setLayout(new BoxLayout(helloRow, BoxLayout.X_AXIS));

        JLabel helloLabel = new JLabel("Hello, ");
        JLabel usernameLabel = this.username;

        helloRow.add(helloLabel);
        helloRow.add(usernameLabel);
        helloRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        center.add(Box.createVerticalStrut(20));
        center.add(helloRow);           // ← 放一行
        center.add(Box.createVerticalGlue());
        center.add(logoutButton);
        center.add(Box.createVerticalStrut(20));

        // ===== 底部导航栏 =====
        JPanel bottomNav = new JPanel();
        bottomNav.setLayout(new GridLayout(1, 2));

        petpending = new JButton("Pet");
        homepage = new JButton("Homepage");

        /**
         *linking the list of pets to the Pet button on the homepage
         */
        petpending.addActionListener(e -> {
            PetDataAccessInterface gateway = new APIInterface();
            PetListViewModel viewModel = new PetListViewModel();
            ViewPetsOutputBoundary presenter = new ViewPetsPresenter(viewModel);
            ViewPetsInputBoundary interactor =
                    new ViewPetsInteractor(gateway, presenter);
            ViewPetsController controller = new ViewPetsController(interactor);
            new PetListView(viewModel, controller);
        });






        bottomNav.add(petpending);
        bottomNav.add(homepage);

        // ===== 加到主界面 =====
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

    // add start 20251124
    // ===== 动态更新 username 和 窗口标题 =====
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
        	// 动态更新 username
            final HomepageState state = (HomepageState) evt.getNewValue();
            this.username.setText(state.getUsername());
            // 动态更新 窗口标题
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (frame != null) {
                frame.setTitle("Homepage - " + homepageViewModel.getState().getUsername());
            }            
        }
//        else if (evt.getPropertyName().equals("password")) {
//            final LoggedInState state = (LoggedInState) evt.getNewValue();
//            if (state.getPasswordError() == null) {
//                JOptionPane.showMessageDialog(this, "password updated for " + state.getUsername());
//                passwordInputField.setText("");
//            }
//            else {
//                JOptionPane.showMessageDialog(this, state.getPasswordError());
//            }
//        }

    }

}
