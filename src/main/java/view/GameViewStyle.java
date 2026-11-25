package view;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** Shared type, palette, and spacing for the match screen. */
final class GameViewStyle {
    static final Color BACKGROUND = new Color(0x141217);
    static final Color SURFACE = new Color(0x28242C);
    static final Color CARD = SURFACE;
    static final Color LINE = new Color(0x88FFFFFF, true);
    static final Color TEXT = Color.WHITE;
    private static final Color PASS_FILL = new Color(0, 0, 0, 160);
    static final Color TILE_LIGHT = new Color(0x2C2931);
    static final Color TILE_DARK = new Color(0x201D25);
    static final Color TILE_HIGHLIGHT = new Color(0x44FFFFFF, true);
    static final Color RANGE_HIGHLIGHT = new Color(0x606C9FF3, true);
    static final Color RANGE_OUTLINE = new Color(0xAFCAFF);
    static final Color ARROW_COLOR = TEXT;
    static final Color ARROW_OUTLINE = new Color(0xB024202C, true);
    static final Color TRAJECTORY_GLOW = new Color(0xB0C99FFF, true);
    static final Color TRAJECTORY_CORE = TEXT;
    static final Color HIT_FLASH = new Color(0xB8FFD29A, true);
    static final Color BOARD_CARD_OUTLINE = BACKGROUND;

    static final Font TEXT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    static final Font HP_FONT = TEXT_FONT.deriveFont(8f);

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

    static JLabel label(String text) {
        JLabel label = new JLabel();
        // Card and player names are literal text, even if they start with "<html>".
        label.putClientProperty("html.disable", true);
        label.setText(text);
        label.setFont(TEXT_FONT);
        label.setForeground(TEXT);
        return label;
    }

    static JPanel section(LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
        return panel;
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
        button.setFont(TEXT_FONT);
        button.setForeground(TEXT);
        button.setOpaque(false);
        button.setFocusable(false);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
    }

    static JButton button(String text) {
        JButton button = new JButton(text);
        configureButton(button);
        button.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        return button;
    }

    static JButton passButton() {
        JButton button = new JButton("Pass") {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics background = graphics.create();
                background.setColor(PASS_FILL);
                background.fillRect(0, 0, getWidth(), getHeight());
                background.dispose();
                super.paintComponent(graphics);
            }
        };
        configureButton(button);
        button.setBorder(BorderFactory.createLineBorder(TEXT));
        button.setBorderPainted(true);
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
