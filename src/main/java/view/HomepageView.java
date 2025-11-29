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
    private UserLogoutController userLogoutController;

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

        logoutButton.addActionListener(
                evt -> {
                    if(evt.getSource().equals(logoutButton)){
                        this.userLogoutController.execute();
                    }
                }
        );
        
        // ===== 底部导航栏 =====
        JPanel bottomNav = new JPanel();
        bottomNav.setLayout(new GridLayout(1, 2));

        petpending = new JButton("Pet");
        homepage = new JButton("Homepage");

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
    
    public void setUserLogoutController(UserLogoutController userLogoutController) {
        this.userLogoutController = userLogoutController;
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
