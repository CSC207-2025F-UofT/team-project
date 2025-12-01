package view;

import javax.swing.*;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewModel;
import interface_adapter.homepage.HomepageState;
import interface_adapter.homepage.LoginChooseViewModel;
import interface_adapter.user_login.UserLoginState;
import interface_adapter.user_login.UserLoginViewModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class LoginChooseView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "loginChoose";

    // 使用你的架构的 ViewModel（不要再定义局部变量遮蔽）
    private final LoginChooseViewModel loginChooseViewModel;
//    private final UserLoginViewModel userLoginViewModel;

//    public LoginChooseView(LoginChooseView loginChooseView) {
    public LoginChooseView(LoginChooseViewModel loginChooseViewModel) {
        this.loginChooseViewModel  = loginChooseViewModel ;
//        this.viewManagerModel = viewManagerModel;

        // 让这个 View 监听自己的 ViewModel
        this.loginChooseViewModel.addPropertyChangeListener(this);
//        this.userLoginViewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // 创建按钮
        JButton userLogin = new JButton("User Login");
        JButton createAccount = new JButton("Create User Account");
        JButton adminLogin = new JButton("Admin Login");

        // 让按钮宽度一致
        int maxWidth = Math.max(userLogin.getPreferredSize().width,
                Math.max(createAccount.getPreferredSize().width,
                        adminLogin.getPreferredSize().width));

        userLogin.setMaximumSize(new Dimension(maxWidth, userLogin.getPreferredSize().height));
        createAccount.setMaximumSize(new Dimension(maxWidth, createAccount.getPreferredSize().height));
        adminLogin.setMaximumSize(new Dimension(maxWidth, adminLogin.getPreferredSize().height));

        userLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        createAccount.setAlignmentX(Component.CENTER_ALIGNMENT);
        adminLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 按钮事件 → 设置 state → 通知 viewManager 切换画面
        userLogin.addActionListener(e -> {
        	
//        	loginChooseViewModel.setState(null);
//        	loginChooseViewModel.firePropertyChange("userLogin");
        	loginChooseViewModel.firePropertyChange("userLogin");
        });

        createAccount.addActionListener(e -> {
//        	viewManagerModel.setState("createAccount");
        	loginChooseViewModel.firePropertyChange("switchToCreateAccount");
        });

        adminLogin.addActionListener(e -> {
//        	viewManagerModel.setState("adminLogin");
        	loginChooseViewModel.firePropertyChange("switchToAdminLogin");
        });

        add(Box.createVerticalStrut(20));
        add(userLogin);
        add(Box.createVerticalStrut(20));
        add(createAccount);
        add(Box.createVerticalStrut(20));
        add(adminLogin);
    }

    public String getViewName() {
        return viewName;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Click " + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
    	String name = "log in";
        if ("userLogin".equals(evt.getPropertyName())) {
        	name = "log in";
        }
        else if ("switchToCreateAccount".equals(evt.getPropertyName())) {
        	name = "CreateUserAccount";
        }else {
        	name = "log in";        	
        }
        // 如果你需要基于自己的 ViewModel 切换
        // 动态更新 窗口标题        	
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (frame != null) {
            frame.setTitle("User Login");
            Container parent = getParent();
            if (parent != null && parent.getLayout() instanceof CardLayout) {
                CardLayout cl = (CardLayout) parent.getLayout();
                cl.show(parent, name);   // 注意：这个必须 EXACT MATCH
            }
        }          

	}
//    @Override
//    public void propertyChange(PropertyChangeEvent evt) {
//        final UserLoginState state = (UserLoginState) evt.getNewValue();
//        setFields(state);
//        usernameErrorField.setText(state.getLoginError());
//    }    
}
