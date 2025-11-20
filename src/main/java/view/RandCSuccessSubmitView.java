package view;

import interface_adapter.RandC_success_submit.RandCSuccessState;
import interface_adapter.RandC_success_submit.RandCSuccessViewModel;
import interface_adapter.ViewManagerModel;
import interface_adapter.clicking.ClickingState;
import interface_adapter.clicking.ClickingViewModel;
import interface_adapter.home.HomeState;
import interface_adapter.home.HomeViewModel;
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
    private JButton homeButton;
    private JLabel messageLabel;
    private ViewManagerModel viewManagerModel;
    private RandCSuccessViewModel randCSuccessViewModel;
    private ClickingViewModel clickingViewModel;
    private HomeViewModel homeViewModel;
    private String viewname = "RandC";

    // 构造函数：从外部传入电影名
    public RandCSuccessSubmitView(ViewManagerModel viewManagerModel, RandCSuccessViewModel randCSuccessViewModel,
                                  ClickingViewModel clickingViewModel, HomeViewModel homeViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.randCSuccessViewModel = randCSuccessViewModel;
        this.clickingViewModel = clickingViewModel;
        this.homeViewModel = homeViewModel;
        this.randCSuccessViewModel.addPropertyChangeListener(this);
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

        // Return 按钮，返回clicking电影界面
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
                // TODO: 切换回clicking界面, 注意确保clicking界面原有数据还在
                viewManagerModel.setState(clickingViewModel.getViewName());
                viewManagerModel.firePropertyChange();

            }
        });
        // Home Button, 跳转到主页
        homeButton = new JButton("Home");
        homeButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        homeButton.setBackground(new Color(70, 130, 180));
        homeButton.setForeground(Color.WHITE);
        homeButton.setFocusPainted(false);
        homeButton.setPreferredSize(new Dimension(100, 40));

        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //清空viewmodel和对应view
                randCSuccessViewModel.setState(new RandCSuccessState());
                randCSuccessViewModel.firePropertyChange();
                //清空clicking view
                clickingViewModel.setState(new ClickingState());
                clickingViewModel.firePropertyChange();
                //切换到homeview
                viewManagerModel.setState(homeViewModel.getViewName());
                viewManagerModel.firePropertyChange();
                //TODO清除clickingpage

            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.add(returnButton);
        bottomPanel.add(homeButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        RandCSuccessState state = (RandCSuccessState) evt.getNewValue();
        movieName = state.getMedianame();
        messageLabel.setText(
                "<html><div style='text-align:center;'>You have submitted the rate and comment for<br>"
                        + "<b>\"" + movieName + "\"</b> successfully.</div></html>"
        );
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
