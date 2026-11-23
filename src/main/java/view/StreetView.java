package view;

import java.awt.Font;
import java.awt.GridLayout;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class StreetView extends JPanel {
    public StreetView(Consumer<String> navigate) {
        setLayout(new GridLayout(0, 1, 12, 12));
        setBorder(BorderFactory.createEmptyBorder(30, 160, 30, 160));

        JLabel title = new JLabel("St. George Street", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        add(title);
        addBuilding("Registrar's Office", "registrar", navigate);
        addBuilding("Lecture Hall", "lecture", navigate);
        addBuilding("Exam Centre", "exam", navigate);
        addBuilding("Innis Residence", "residence", navigate);
        addBuilding("Title Screen", "title", navigate);
    }

    private void addBuilding(String name, String page, Consumer<String> navigate) {
        JButton button = new JButton(name);
        button.addActionListener(event -> navigate.accept(page));
        add(button);
    }
}
