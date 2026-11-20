package view;

import javax.swing.*;
import java.awt.*;

public class HistoryPanel extends JPanel {
    public HistoryPanel(Navigator nav) {
        setLayout(new BorderLayout());
        add(new HeaderBar(nav, ScreenNames.CAREGIVER_MENU, "Communication History"),
                BorderLayout.NORTH);

        // TODO UC4: load real messages via the view history use case
        JTextArea text = new JTextArea(
                "TEMPORARY Today\n10:00 AM  I want water.\n11:00 AM  I want play.\n");
        text.setEditable(false);
        text.setFont(text.getFont().deriveFont(16f));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        wrapper.add(new JScrollPane(text), BorderLayout.CENTER);
        add(wrapper, BorderLayout.CENTER);
    }
}