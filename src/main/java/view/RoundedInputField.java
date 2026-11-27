package view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

/**
 * A rounded, see-through box that holds a text field, with an icon on the left
 * and grey placeholder text while the field is empty.
 */
public class RoundedInputField extends JPanel {

    /** Which icon to draw on the left of the box. */
    public enum IconType { USER, LOCK }

    private static final int ICON_AREA = 54;     // space on the left for the icon
    private static final Color TEXT = new Color(255, 255, 255, 235);
    private static final Color HINT = new Color(255, 255, 255, 210);

    private final JTextField field;
    private final String placeholder;
    private final IconType iconType;

    public RoundedInputField(JTextField field, String placeholder, IconType iconType) {
        this.field = field;
        this.placeholder = placeholder;
        this.iconType = iconType;

        setOpaque(false);                       // we paint our own background
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, ICON_AREA, 0, 16));
        setPreferredSize(new Dimension(380, 60));
        setMaximumSize(new Dimension(380, 60));
        setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));

        field.setOpaque(false);
        field.setBorder(null);
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        add(field, BorderLayout.CENTER);

        field.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { repaint(); }
            public void removeUpdate(DocumentEvent e) { repaint(); }
            public void changedUpdate(DocumentEvent e) { repaint(); }
        });
        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { repaint(); }
            public void focusLost(FocusEvent e) { repaint(); }
        });

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { field.requestFocusInWindow(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        boolean focused = field.isFocusOwner();
        RoundRectangle2D box = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);

        // See-through white box (a little brighter when selected)
        g2.setColor(new Color(255, 255, 255, focused ? 80 : 60));
        g2.fill(box);
        if (focused) {
            g2.setColor(new Color(255, 190, 90, 170));   // soft orange outline
            g2.setStroke(new BasicStroke(1.5f));
            g2.draw(box);
        }

        paintIcon(g2);

        // Placeholder text, only while the field is empty
        if (field.getDocument().getLength() == 0) {
            g2.setColor(HINT);
            g2.setFont(field.getFont());
            FontMetrics fm = g2.getFontMetrics();
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, ICON_AREA, y);
        }
        g2.dispose();
    }

    /** Draws a simple outline icon: a person or a padlock. */
    private void paintIcon(Graphics2D g2) {
        g2.setColor(HINT);
        g2.setStroke(new BasicStroke(1.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int cx = 29;                 // centre of the icon
        int cy = getHeight() / 2;

        if (iconType == IconType.USER) {
            g2.draw(new Ellipse2D.Float(cx - 5, cy - 10, 10, 10));                     // head
            g2.draw(new Arc2D.Float(cx - 9, cy + 1, 18, 18, 0, 180, Arc2D.OPEN));     // shoulders
        }
        else {
            g2.draw(new Arc2D.Float(cx - 5, cy - 11, 10, 12, 0, 180, Arc2D.OPEN));    // shackle
            g2.drawLine(cx - 5, cy - 5, cx - 5, cy - 2);
            g2.drawLine(cx + 5, cy - 5, cx + 5, cy - 2);
            g2.draw(new RoundRectangle2D.Float(cx - 8, cy - 2, 16, 13, 3, 3));        // body
            g2.fill(new Ellipse2D.Float(cx - 1.5f, cy + 2, 3, 3));                    // keyhole
            g2.drawLine(cx, cy + 5, cx, cy + 7);
        }
    }
}