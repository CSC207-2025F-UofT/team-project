package view.journal;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;
import javax.swing.JTextArea;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/** A wrapping editor that accepts changes only while the rendered text fits. */
final class JournalTextArea extends JTextArea {
    private final String placeholderText;

    JournalTextArea(String accessibleName, String initialText, String placeholderText,
                    int textAreaWidth, int textAreaHeight) {
        super(initialText);
        this.placeholderText = placeholderText;
        Font handwriting = JournalStyle.HANDWRITING.deriveFont(14f);
        configureWrappingTextArea(this, handwriting);
        setOpaque(false);
        getAccessibleContext().setAccessibleName(accessibleName);

        JTextArea textFitMeasurementArea = new JTextArea();
        configureWrappingTextArea(textFitMeasurementArea, handwriting);
        textFitMeasurementArea.setSize(textAreaWidth, textAreaHeight);
        ((AbstractDocument) getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass bypass, int offset, String text,
                                     AttributeSet attributes) throws BadLocationException {
                replace(bypass, offset, 0, text, attributes);
            }

            @Override
            public void replace(FilterBypass bypass, int offset, int length, String text,
                                AttributeSet attributes) throws BadLocationException {
                String existingText = getText();
                String replacementText = text == null ? "" : text;
                textFitMeasurementArea.setText(existingText.substring(0, offset) + replacementText
                        + existingText.substring(offset + length));
                if (replacementText.isEmpty()
                        || textFitMeasurementArea.getPreferredSize().height <= textAreaHeight) {
                    bypass.replace(offset, length, replacementText, attributes);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
    }

    private static void configureWrappingTextArea(JTextArea textArea, Font font) {
        textArea.setFont(font);
        textArea.setForeground(JournalStyle.INK);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setMargin(new Insets(3, 3, 3, 3));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (getDocument().getLength() == 0 && !placeholderText.isEmpty()) {
            graphics.setColor(JournalStyle.INK);
            graphics.setFont(getFont());
            graphics.drawString(placeholderText, getInsets().left,
                    getInsets().top + graphics.getFontMetrics().getAscent());
        }
    }
}
