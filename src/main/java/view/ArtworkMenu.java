package view;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/** Lays out controls in the coordinate space of the 2048×2048 menu artwork. */
abstract class ArtworkMenu extends JPanel {
    private static final int ARTWORK_SIZE = 2048;
    private final BufferedImage artwork;
    private final List<Placement> controls = new ArrayList<>();
    private final JTextArea error = new JTextArea();

    ArtworkMenu(String background, int errorX, int errorY, int errorWidth, int errorHeight) {
        setLayout(null);
        setBackground(Color.BLACK);
        artwork = GameArtwork.screenBackground(background);

        error.setName("error");
        error.setOpaque(false);
        error.setEditable(false);
        error.setFocusable(false);
        error.setLineWrap(true);
        error.setWrapStyleWord(true);
        error.setForeground(GameViewStyle.TEXT);
        error.setBorder(null);
        place(error, errorX, errorY, errorWidth, errorHeight, 42);
    }

    final JButton addButton(String name, Runnable action, int x, int y, int width, int height) {
        JButton button = GameViewStyle.button(artwork == null ? name : "");
        button.setName(name);
        button.addActionListener(event -> action.run());
        place(button, x, y, width, height, 60);
        return button;
    }

    final JTextField addField(String name, int x, int y, int width, int height) {
        JTextField field = new JTextField();
        field.setName(name);
        field.setOpaque(false);
        field.setBorder(null);
        field.setForeground(GameViewStyle.TEXT);
        field.setCaretColor(GameViewStyle.TEXT);
        place(field, x, y, width, height, 85);
        return field;
    }

    final JLabel addText(int x, int y, int width, int height) {
        JLabel label = GameViewStyle.label("");
        place(label, x, y, width, height, 72);
        return label;
    }

    final void showError(String message) {
        error.setText(message);
    }

    final Runnable submitField(JTextField field, String emptyMessage, Consumer<String> receive) {
        Runnable submit = () -> {
            String value = field.getText().strip();
            if (value.isEmpty()) {
                showError(emptyMessage);
            } else {
                showError("");
                receive.accept(value);
            }
        };
        field.addActionListener(event -> submit.run());
        return submit;
    }

    private void place(JComponent control, int x, int y, int width, int height, int fontSize) {
        controls.add(new Placement(control, new Rectangle(x, y, width, height), fontSize));
        add(control);
    }

    @Override
    public void doLayout() {
        Rectangle bounds = artworkBounds();
        double scale = (double) bounds.width / ARTWORK_SIZE;
        for (Placement placement : controls) {
            Rectangle position = placement.bounds();
            placement.control().setBounds(
                    bounds.x + (int) Math.round(position.x * scale),
                    bounds.y + (int) Math.round(position.y * scale),
                    (int) Math.round(position.width * scale),
                    (int) Math.round(position.height * scale));
            placement.control().setFont(GameViewStyle.TEXT_FONT.deriveFont(Math.max(1f, (float) (placement.fontSize() * scale))));
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        GameArtwork.paintBackground(graphics, artwork, artworkBounds());
    }

    private Rectangle artworkBounds() {
        int side = Math.min(getWidth(), getHeight());
        return new Rectangle((getWidth() - side) / 2, (getHeight() - side) / 2, side, side);
    }

    private record Placement(JComponent control, Rectangle bounds, int fontSize) {
    }
}
