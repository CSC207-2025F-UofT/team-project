package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RandCSuccessSubmitView extends JPanel {
    private String movieName;
    private JButton returnButton;
    private JLabel messageLabel;

    // 构造函数：从外部传入电影名
    public RandCSuccessSubmitView(String movieName) {
        this.movieName = movieName;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(245, 245, 245));

        // 提示文字
        messageLabel = new JLabel(
                "<html><div style='text-align:center;'>You have submitted the rate and comment for<br>"
                        + "<b>\"" + movieName + "\"</b> successfully.</div></html>",
                SwingConstants.CENTER
        );
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        messageLabel.setForeground(new Color(40, 40, 40));
        add(messageLabel, BorderLayout.CENTER);

        // Return 按钮
        returnButton = new JButton("Return");
        returnButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        returnButton.setBackground(new Color(70, 130, 180));
        returnButton.setForeground(Color.WHITE);
        returnButton.setFocusPainted(false);
        returnButton.setPreferredSize(new Dimension(100, 40));

        // 点击事件（此处留空逻辑，未来可替换为切换 UI）
        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: 切换回主界面，例如：
                // mainFrame.setContentPane(previousPanel);
                // mainFrame.revalidate();
                JOptionPane.showMessageDialog(
                        RandCSuccessSubmitView.this,
                        "Return button clicked (you can switch UI here).",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.add(returnButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    // 测试运行
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Submission Success");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 200);
            frame.setLocationRelativeTo(null);
            frame.add(new RandCSuccessSubmitView("Inception")); // 传入电影名
            frame.setVisible(true);
        });
    }
}
