package stockprototype.comparison.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.table.DefaultTableCellRenderer;

/** Colors and controls scoped to the comparison screen. */
final class ComparisonTheme {
    static final Color BACKGROUND = Color.BLACK;
    static final Color SURFACE = new Color(23, 25, 29);
    static final Color TEXT = new Color(236, 238, 241);
    static final Color MUTED = new Color(169, 176, 185);
    static final Color GRID = new Color(61, 66, 74);
    static final Color GAIN = new Color(77, 221, 147);
    static final Color LOSS = new Color(255, 115, 126);

    private ComparisonTheme() { }

    static Color changeColor(int sign) {
        if (sign > 0) return GAIN;
        if (sign < 0) return LOSS;
        return TEXT;
    }

    static JPanel panel(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(BACKGROUND);
        return panel;
    }

    static JLabel label(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        return label;
    }

    static void field(JTextField field) {
        field.setBackground(SURFACE);
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setDisabledTextColor(MUTED);
        outline(field, 5, 8);
    }

    static void button(JButton button) {
        button.setUI(new BasicButtonUI());
        button.setOpaque(true);
        button.setBackground(TEXT);
        button.setForeground(BACKGROUND);
        button.setFont(button.getFont().deriveFont(Font.BOLD));
        outline(button, 5, 14);
    }

    static void period(JComboBox<?> combo) {
        combo.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                return new BasicArrowButton(SwingConstants.SOUTH, SURFACE, SURFACE, TEXT, SURFACE);
            }
        });
        combo.setBackground(SURFACE);
        combo.setForeground(TEXT);
        outline(combo, 1, 4);
    }

    private static void outline(JComponent component, int vertical, int horizontal) {
        Border padding = BorderFactory.createEmptyBorder(vertical, horizontal, vertical, horizontal);
        Border unfocused = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(GRID), padding);
        Border focused = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(MUTED, 1), padding);
        component.setBorder(unfocused);
        component.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent event) { component.setBorder(focused); }
            @Override
            public void focusLost(FocusEvent event) { component.setBorder(unfocused); }
        });
    }

    static void tabs(JTabbedPane tabs) {
        tabs.setBackground(BACKGROUND);
        tabs.setForeground(TEXT);
        tabs.setUI(new BasicTabbedPaneUI() {
            @Override
            protected void installDefaults() {
                super.installDefaults();
                highlight = lightHighlight = shadow = darkShadow = GRID;
                focus = MUTED;
            }
            @Override
            protected void paintTabBackground(Graphics g, int placement, int index, int x, int y,
                                               int width, int height, boolean selected) {
                g.setColor(selected ? SURFACE : BACKGROUND);
                g.fillRect(x, y, width, height);
            }
            @Override
            protected void paintContentBorder(Graphics g, int placement, int selectedIndex) { }
        });
    }

    static void table(JTable table) {
        table.setForeground(TEXT);
        table.setBackground(SURFACE);
        table.setSelectionBackground(GRID);
        table.setSelectionForeground(TEXT);
        table.setGridColor(GRID);
        table.setShowVerticalLines(false);
        table.setRowHeight(26);
        DefaultTableCellRenderer header = new DefaultTableCellRenderer();
        header.setBackground(BACKGROUND);
        header.setForeground(MUTED);
        header.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
        table.getTableHeader().setDefaultRenderer(header);
        table.getTableHeader().setBackground(BACKGROUND);
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean selected,
                                                           boolean focus, int row, int column) {
                super.getTableCellRendererComponent(t, value, selected, focus, row, column);
                setBorder(BorderFactory.createCompoundBorder(getBorder(),
                        BorderFactory.createEmptyBorder(2, 5, 2, 5)));
                return this;
            }
        });
    }
}
