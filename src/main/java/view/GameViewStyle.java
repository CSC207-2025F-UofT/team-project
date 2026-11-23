package view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** Shared type, palette, and spacing for the match screen. */
final class GameViewStyle {
    static final Color BACKGROUND = new Color(0x7859A5);
    static final Color SURFACE = new Color(0xB59FD3);
    static final Color CARD = new Color(0xB59FD3);
    static final Color HOVER = new Color(0xD7C7EF);
    static final Color LINE = new Color(0xA77FCD);
    static final Color TEXT = new Color(0x7859A5);
    static final Color MUTED = new Color(0x7859A5);
    static final Color GOLD = new Color(0xED63FF);
    static final Color TILE_LIGHT = new Color(0x2B2438);
    static final Color TILE_DARK = new Color(0x271E2F);
    static final Color TILE_HIGHLIGHT = new Color(0x55E692F3, true);
    static final Color RANGE_HIGHLIGHT = new Color(0x606C9FF3, true);
    static final Color RANGE_OUTLINE = new Color(0xAFCAFF);
    static final Color ARROW_COLOR = new Color(0xEAD9FF);
    static final Color ARROW_OUTLINE = new Color(0xB04A2870, true);
    static final Color TRAJECTORY_GLOW = new Color(0xB0C99FFF, true);
    static final Color TRAJECTORY_CORE = new Color(0xFFF0FF);
    static final Color HIT_FLASH = new Color(0xB8FFD29A, true);
    static final Color BOARD_CARD_OUTLINE = new Color(0x16131B);
    static final Color HP_TEXT = new Color(0xE4F2F2);

    static final Font TURN_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 13);
    static final Font BUTTON_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 13);
    static final Font COUNT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
    static final Font TIMER_FONT = new Font(Font.MONOSPACED, Font.BOLD, 13);
    // HP font size at hand-card height. Other card sizes scale it proportionally.
    static final Font HP_FONT = new Font(Font.SANS_SERIF, Font.ITALIC, 8);

    static final int GAP = 10;
    static final int CARD_GAP = 6;
    static final int PAGE_PADDING = 12;
    static final int PREVIEW_WIDTH = 338;
    static final int HAND_CARD_HEIGHT = 168;
    static final int SPELL_PREVIEW_HEIGHT = 120;
    static final int TRAJECTORY_MILLIS = 450;
    static final int HIT_FLASH_MILLIS = 380;

    private GameViewStyle() {
    }

    static JLabel label(String text, Font font, Color color) {
        JLabel label = new JLabel();
        // Card and player names are literal text, even if they start with "<html>".
        label.putClientProperty("html.disable", true);
        label.setText(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    static JPanel section(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBackground(SURFACE);
        panel.setBorder(outline(LINE, GAP, GAP));
        return panel;
    }

    static Border outline(Color color, int vertical, int horizontal) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color),
                BorderFactory.createEmptyBorder(vertical, horizontal, vertical, horizontal));
    }

    static JScrollPane scroll(JComponent content, int vertical, int horizontal) {
        JScrollPane pane = new JScrollPane(content, vertical, horizontal);
        pane.setBorder(BorderFactory.createEmptyBorder());
        pane.setOpaque(false);
        pane.getViewport().setOpaque(false);
        pane.getVerticalScrollBar().setUnitIncrement(20);
        pane.getHorizontalScrollBar().setUnitIncrement(20);
        pane.getVerticalScrollBar().setUI(new MatchScrollBarUI());
        pane.getHorizontalScrollBar().setUI(new MatchScrollBarUI());
        return pane;
    }

    static void configureButton(JButton button) {
        button.putClientProperty("html.disable", true);
        button.setUI(new BasicButtonUI());
        button.setFont(BUTTON_FONT);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);
    }

    static void inspectOnHover(JButton button, Runnable inspect) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent event) {
                inspect.run();
            }
        });
        button.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent event) {
                inspect.run();
            }
        });
    }

    static JButton button(String text, boolean primary) {
        JButton button = new JButton();
        configureButton(button);
        button.setText(text);
        button.setOpaque(true);
        button.setForeground(primary ? BACKGROUND : TEXT);
        Runnable updateStyle = () -> {
            Color fill = primary ? GOLD : CARD;
            if (button.getModel().isArmed()) {
                fill = primary ? GOLD.darker() : HOVER.darker();
            } else if (button.getModel().isRollover()) {
                fill = primary ? GOLD.brighter() : HOVER;
            }
            button.setBackground(fill);
            Color border = button.hasFocus() ? TEXT : primary ? GOLD : LINE;
            button.setBorder(outline(border, 5, 5));
        };
        button.getModel().addChangeListener(event -> updateStyle.run());
        button.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent event) {
                updateStyle.run();
            }

            @Override
            public void focusLost(FocusEvent event) {
                updateStyle.run();
            }
        });
        updateStyle.run();
        return button;
    }

    private static final class MatchScrollBarUI extends BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            trackColor = SURFACE;
            thumbColor = LINE;
            thumbHighlightColor = LINE;
            thumbLightShadowColor = LINE;
            thumbDarkShadowColor = LINE;
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return hiddenButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return hiddenButton();
        }

        private JButton hiddenButton() {
            JButton button = new JButton();
            Dimension size = new Dimension(0, 0);
            button.setPreferredSize(size);
            button.setMinimumSize(size);
            button.setMaximumSize(size);
            return button;
        }
    }
}
