package org.accessgraph.view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Arrays;

final class Ui {
    static final Color INK = new Color(0x142B2D);
    static final Color PINE = new Color(0x103D3C);
    static final Color JADE = new Color(0x0F625D);
    static final Color MINT = new Color(0xCFEADF);
    static final Color PAPER = new Color(0xF5F7F3);
    static final Color LINE = new Color(0xCAD7D3);
    static final Color MUTED = new Color(0x4A6461);
    static final Color ERROR = new Color(0x9B292E);
    private static final Color FOCUS = new Color(0x2776DA);
    private static final String FONT = Arrays.stream(new String[]{"Avenir Next", "Segoe UI"})
            .filter(name -> new Font(name, Font.PLAIN, 12).getFamily().equals(name)).findFirst().orElse(Font.SANS_SERIF);

    private Ui() { }

    static Font font(int size, boolean bold) { return new Font(FONT, bold ? Font.BOLD : Font.PLAIN, size); }

    static JLabel label(String text, int size, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(font(size, bold));
        label.setForeground(INK);
        return label;
    }

    static JButton button(String text, boolean primary) {
        return button(text, primary, 14, 9);
    }

    static JButton button(String text, boolean primary, int horizontal, int vertical) {
        JButton button = new JButton(text);
        button.setFont(font(14, true));
        button.setForeground(primary ? Color.WHITE : INK);
        button.setBackground(primary ? JADE : Color.WHITE);
        button.setOpaque(true);
        setControlBorder(button, primary ? JADE : LINE, horizontal, vertical);
        return button;
    }

    static void styleField(JTextField field) {
        field.setFont(font(16, false));
        field.setForeground(INK);
        field.setBackground(Color.WHITE);
        setControlBorder(field, LINE, 11, 9);
    }

    private static void setControlBorder(JComponent component, Color color, int horizontal, int vertical) {
        Border standard = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(color),
                BorderFactory.createEmptyBorder(vertical, horizontal, vertical, horizontal));
        Border focused = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(FOCUS, 2),
                BorderFactory.createEmptyBorder(vertical - 1, horizontal - 1, vertical - 1, horizontal - 1));
        component.setBorder(standard);
        component.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent event) { component.setBorder(focused); }
            @Override public void focusLost(FocusEvent event) { component.setBorder(standard); }
        });
    }
}
