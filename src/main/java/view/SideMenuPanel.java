package view;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import service.Backend;
import service.Frontend;

public class SideMenuPanel extends JPanel {

    private final Frontend frontend;
    private final Backend backend;
    private String userType;

    public SideMenuPanel(Frontend frontend, Backend backend, String userType) {
        this.frontend = frontend;
        this.backend = backend;
        this.userType = userType;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(0xF5F7FB));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 显示标题
        JLabel title = new JLabel("Hello, " + capitalize(userType));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title);
        add(Box.createRigidArea(new Dimension(0, 20)));

        // 加载菜单配置
        Map<String, String[]> config = MenuConfig.getMenuItems();
        String[] items = config.getOrDefault(userType, new String[]{"Log Out"});

        // 依据配置自动生成按钮
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

        // 点击回调（你可以根据需要扩展）
        btn.addActionListener(e -> handleMenuClick(text));

        return btn;
    }

    private void handleMenuClick(String item) {
        switch (item) {
            case "Manage Applications":
                frontend.showDashboard("admin");
                break;
            case "View Pets":
//                frontend.showPetListPage();
                break;
            case "Add Pet":
//                frontend.showAddPetPage();
                break;
            case "Modify Pet":
//                frontend.showModifyPetPage();
                break;
            case "Delete Pet":
//                frontend.showDeletePetPage();
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
