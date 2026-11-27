package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A rounded orange button that brightens when the mouse is over it.
 */
public class RoundedButton extends JButton {

    private static final Color ORANGE = new Color(232, 136, 0);
    private static final Color ORANGE_HOVER = new Color(250, 158, 25);
    private static final Color ORANGE_PRESSED = new Color(205, 118, 0);

    private boolean hovered = false;

    public RoundedButton(String text, int width, int height) {
        super(text);

        // Turn off Swing's default button look so we can paint our own
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);

        setForeground(Color.WHITE);
        setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));

        // Track the mouse so the button can light up on hover
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovered = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovered = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color fill = ORANGE;
        if (getModel().isPressed()) {
            fill = ORANGE_PRESSED;
        }
        else if (hovered) {
            fill = ORANGE_HOVER;
        }
        g2.setColor(fill);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
        g2.dispose();

        super.paintComponent(g);   // draws the text on top
    }
}
