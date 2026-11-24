package view;

import javax.swing.*;
import java.awt.*;

public class MainBoardPanel extends JPanel {
    private final JTextField messageBar = new JTextField();

    public MainBoardPanel(Navigator nav) {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // top right: acorn opens caregiver mode
        ImageIcon icon = Icons.acorn(40);
        JButton acorn = new JButton(icon);
        if (icon == null) {
            acorn.setText("Caregiver");
        }
        acorn.addActionListener(e -> nav.show(ScreenNames.PIN));
        JPanel top = new JPanel(new BorderLayout());
        top.add(acorn, BorderLayout.EAST);

        // message bar
        messageBar.setEditable(false);
        messageBar.setFont(messageBar.getFont().deriveFont(20f));
        JPanel messageBlock = new JPanel(new BorderLayout());
        messageBlock.add(new JLabel("My message"), BorderLayout.NORTH);
        messageBlock.add(messageBar, BorderLayout.CENTER);

        // pictogram grid (placeholder text buttons)
        JPanel grid = new JPanel(new GridLayout(2, 3, 8, 8));
        for (String label : new String[]{"Water", "Food", "Play", "Toilet", "Sleep", "More"}) {
            JButton b = new JButton(label);
            b.addActionListener(e -> messageBar.setText(messageBar.getText() + label + " "));
            grid.add(b);
        }

        JButton speak = new JButton("Speak");
        speak.addActionListener(e -> System.out.println("TODO speak: " + messageBar.getText()));

        JPanel centre = new JPanel(new BorderLayout(8, 8));
        centre.add(messageBlock, BorderLayout.NORTH);
        centre.add(grid, BorderLayout.CENTER);
        centre.add(speak, BorderLayout.SOUTH);

        // bottom: Help / Stop / Break show in the message bar
        JPanel bottom = new JPanel(new GridLayout(1, 3, 8, 8));
        for (String s : new String[]{"Help", "Stop", "Break"}) {
            JButton b = new JButton(s);
            // TODO core message use case: build and save the message
            b.addActionListener(e -> messageBar.setText(s));
            bottom.add(b);
        }

        add(top, BorderLayout.NORTH);
        add(centre, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }
}