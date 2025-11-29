package service;

import view.LoginChooseView;
import interface_adapter.user_logout.UserLogoutController;

import javax.swing.*;
import java.awt.event.*;

public class Frontend extends JFrame {

    private final Backend backend;

    // 保存 Logout controller
    private UserLogoutController userLogoutController;

    // 保存 AppBuilder 提供的 cardPanel（所有界面靠它切换）
    private JPanel cardPanel;

    public Frontend(Backend backend) {
        this.backend = backend;

        setTitle("Pet Adoption System");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                backend.toJsonFiles("users.json", "pets.json", "admins.json", "applications.json");
                dispose();
                System.exit(0);
            }
        });
    }

    /** AppBuilder 调用这个，把整个 CardPanel 注册进来 */
    public void setCardPanel(JPanel cardPanel) {
        this.cardPanel = cardPanel;
        setContentPane(cardPanel);  // 初始显示 LoginChoose
    }

    /** 给 SideMenuPanel 用 */
    public void setUserLogoutController(UserLogoutController controller) {
        this.userLogoutController = controller;
    }

    /** Dashboard 调用 */
    public void showDashboard(String userType) {
        // Dashboard 也是一个 JPanel（在 cardPanel 外部）
        // 所以切换成 Dashboard 视图
        setContentPane(new view.MainDashboardView(this, backend, userType));
        revalidate();
        repaint();
    }

    /** Logout 按钮调用 */
    public void logout() {
        if (userLogoutController != null) {
            userLogoutController.execute();   // 触发 Clean Architecture 登出用例
        }

        // ★ 登出后恢复 cardPanel（所有登录、注册页面）
        if (cardPanel != null) {
            setContentPane(cardPanel); 
            revalidate();
            repaint();
        }
    }
}
