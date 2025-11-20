package view;

import javax.swing.*;
import java.awt.*;

public class CalendarPanel extends JPanel {
    public CalendarPanel() {
        setLayout(new BorderLayout());
        add(new JLabel("Calendar Page (Placeholder)", SwingConstants.CENTER), BorderLayout.CENTER);
    }
}
