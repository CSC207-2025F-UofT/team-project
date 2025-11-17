package ui;

import javax.swing.*;
import java.awt.*;

public class TrackerView extends JFrame {
    public TrackerView() {
        setTitle("Tracker");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JTextArea stub = new JTextArea("""
                Tracker Section
                ----------------
                TODO: Implement your expense tracker UI here.
                """);
        stub.setEditable(false);
        stub.setMargin(new Insets(8, 8, 8, 8));

        add(new JScrollPane(stub), BorderLayout.CENTER);
    }
}
