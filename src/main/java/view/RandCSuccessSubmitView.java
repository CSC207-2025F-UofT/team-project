package view;

import interface_adapter.RandC_success_submit.RandCSuccessState;
import interface_adapter.RandC_success_submit.RandCSuccessViewModel;
import interface_adapter.clicking.ClickingViewModel;
import interface_adapter.rate_and_comment.CommentState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RandCSuccessSubmitView extends JPanel implements PropertyChangeListener {
    private String movieName;
    private JButton returnButton;
    private JLabel messageLabel;
    private RandCSuccessViewModel randCSuccessViewModel;
    private ClickingViewModel clickingViewModel;
    private String viewname = "RandC";

    // 构造函数：从外部传入电影名
    public RandCSuccessSubmitView(RandCSuccessViewModel randCSuccessViewModel, ClickingViewModel clickingViewModel) {
        this.randCSuccessViewModel = randCSuccessViewModel;
        this.clickingViewModel = clickingViewModel;
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
                //清空此界面及对应的viewmodel
                randCSuccessViewModel.setState(new RandCSuccessState());
                randCSuccessViewModel.firePropertyChange();
                // TODO: 切换回主界面，例如：


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

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        RandCSuccessState state = (RandCSuccessState) evt.getNewValue();
        movieName = state.getMedianame();

    }

    public String getViewName() {
        return this.viewname;
    }

    // 测试运行
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Submission Success");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(500, 200);
//            frame.setLocationRelativeTo(null);
//            frame.add(new RandCSuccessSubmitView("Inception")); // 传入电影名
//            frame.setVisible(true);
//        });
//    }
}
