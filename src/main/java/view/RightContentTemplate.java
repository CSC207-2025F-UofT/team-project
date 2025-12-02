package view;

import javax.swing.*;
import java.awt.*;

public abstract class RightContentTemplate extends JPanel {

    protected final JLabel titleLabel = new JLabel();
    protected final JPanel bodyPanel = new JPanel();   // 内容区，子类往这加东西

    public RightContentTemplate(String titleText) {

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ----- Title -----
        titleLabel.setText(titleText);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        titleLabel.setOpaque(true);
//        titleLabel.setBackground(Color.WHITE);
        titleLabel.setBackground(new Color(0xF0F0F0));

        add(titleLabel, BorderLayout.NORTH);

        // ----- Body -----
        bodyPanel.setLayout(new BoxLayout(bodyPanel, BoxLayout.Y_AXIS));
        bodyPanel.setBackground(Color.WHITE);

        // ----- Scroll -----
        JScrollPane scrollPane = new JScrollPane(bodyPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane, BorderLayout.CENTER);
    }
}
