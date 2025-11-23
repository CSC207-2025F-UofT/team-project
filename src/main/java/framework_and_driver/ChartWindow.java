package framework_and_driver;

import entity.ChartViewModel;
import interface_adapter.IntervalController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ChartWindow extends JFrame {

    private final JLabel chartDisplayLabel = new JLabel("<html>Dow Jones Industrial Average<br>📊 CHART DATA HERE 📈</html>", SwingConstants.CENTER);
    private final JTextArea statusArea = new JTextArea(3, 40); // 用于显示Presenter的输出
    private IntervalController controller;

    public ChartWindow() {
        super("Stock Chart Platform - UC4 Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // --- 1. top controller ---
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // --- 2. chart part ---
        chartDisplayLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        chartDisplayLabel.setVerticalAlignment(SwingConstants.CENTER);
        chartDisplayLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        chartDisplayLabel.setOpaque(true);
        chartDisplayLabel.setBackground(Color.WHITE);
        add(chartDisplayLabel, BorderLayout.CENTER);

        // --- 3. bottom control and stuation ---
        JPanel southPanel = createSouthPanel();
        add(southPanel, BorderLayout.SOUTH);

        pack();
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        // LOGO
        JLabel logo = new JLabel("✶ BILLIONAIRE", SwingConstants.LEFT);
        logo.setFont(new Font("SansSerif", Font.BOLD, 16));

        // login
        JButton loginButton = new JButton("Signup/ Login");
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(Color.BLACK);
        loginButton.setFocusPainted(false);

        panel.add(logo, BorderLayout.WEST);
        panel.add(loginButton, BorderLayout.EAST);
        return panel;
    }

    private JPanel createSouthPanel() {
        JPanel southPanel = new JPanel(new BorderLayout());

        // 按钮组 (5M, 1D, 1W)
        JPanel intervalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        createIntervalButton(intervalPanel, "5M");
        createIntervalButton(intervalPanel, "1D");
        createIntervalButton(intervalPanel, "1W");

        // 状态信息和 Back 按钮
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        controlPanel.add(intervalPanel, BorderLayout.WEST);
        controlPanel.add(statusArea, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.BLACK);
        controlPanel.add(backButton, BorderLayout.EAST);

        southPanel.add(controlPanel, BorderLayout.CENTER);

        return southPanel;
    }

    private void createIntervalButton(JPanel panel, String text) {
        JButton button = new JButton(text);
        button.addActionListener((ActionEvent e) -> {
            // 将按钮点击事件委托给 Controller
            if (controller != null) {
                statusArea.append(">>> Requesting: " + text + "\n");
                controller.handleTimeChange(text);
            }
        });
        panel.add(button);
    }

    // --- Controller Setter ---
    public void setController(IntervalController controller) {
        this.controller = controller;
    }

    // --- Presenter Output ---
    public void updateChart(ChartViewModel viewModel) {
        chartDisplayLabel.setText("<html>Dow Jones Industrial Average<br>📈 " + viewModel.getTitle() + " Updated!</html>");
        statusArea.append("✅ Success: Data Points = " + viewModel.getPrices().size() + "\n");
    }

    public void displayError(String message) {
        JOptionPane.showMessageDialog(this, message, "UC4 Error", JOptionPane.ERROR_MESSAGE);
        statusArea.append("❌ ERROR: " + message + "\n");
    }
}