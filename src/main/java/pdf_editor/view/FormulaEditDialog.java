package pdf_editor.view;

import pdf_editor.use_case.FormulaEditSubmission;
import pdf_editor.domain.PdfMaterial;
import pdf_editor.data_access.MaterialRenderer;
import view.stack.Fonts;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Objects;
import java.util.function.Function;

/** Compact, platform-aware Swing adapter. Calculation is injected; this view does not evaluate math. */
public final class FormulaEditDialog {
    public record Result(String formula, String result) { }

    private static final Color INK = new Color(36, 42, 55);
    private static final Color SECONDARY = new Color(111, 119, 134);
    private static final Color BORDER = new Color(209, 216, 230);
    private static final Color BLUE = new Color(15, 117, 236);
    private FormulaEditDialog() { }

    /** Compatibility for standalone consumers that do not yet inject US2. */
    public static Result show(Window owner, PdfMaterial item) {
        return show(owner, item, latex -> item.result());
    }

    public static Result show(Window owner, PdfMaterial item, Function<String, String> recalculate) {
        Objects.requireNonNull(item, "item");
        Objects.requireNonNull(recalculate, "recalculate");
        if (item.isGraph()) throw new IllegalArgumentException("Graphs cannot be edited as formulas");
        JDialog dialog = new JDialog(owner, Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setTitle("Edit Formula");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        boolean roundedWindow = canUseTranslucency();
        if (roundedWindow) {
            dialog.setUndecorated(true);
            try { dialog.setBackground(new Color(0, 0, 0, 0)); }
            catch (UnsupportedOperationException unsupported) { roundedWindow = false; }
        }

        JPanel surface = new RoundedSurface();
        surface.setLayout(new BorderLayout(0, 13));
        surface.setBorder(BorderFactory.createEmptyBorder(20, 22, 18, 22));
        JLabel title = new JLabel("Edit formula");
        title.setForeground(INK);
        title.setFont(Fonts.ui(Font.BOLD, 18f));
        JLabel subtitle = new JLabel("Choose how to save this expression.");
        subtitle.setForeground(SECONDARY);
        subtitle.setFont(Fonts.ui(Font.PLAIN, 12f));
        JPanel titleArea = new JPanel(new BorderLayout(0, 4));
        titleArea.setOpaque(false);
        titleArea.add(title, BorderLayout.NORTH);
        titleArea.add(subtitle, BorderLayout.SOUTH);

        JComboBox<String> mode = new JComboBox<>(new String[] {"Calculate (LaTeX)", "Manual Entry"});
        mode.setFont(Fonts.ui(Font.PLAIN, 12f));
        mode.setPreferredSize(new Dimension(174, 29));
        mode.setToolTipText("Calculate with US2, or save your own expression without calculation");
        JPanel heading = new JPanel(new BorderLayout(8, 0));
        heading.setOpaque(false);
        heading.add(titleArea, BorderLayout.CENTER);
        heading.add(mode, BorderLayout.EAST);
        surface.add(heading, BorderLayout.NORTH);

        JLabel formulaLabel = new JLabel("FORMULA");
        formulaLabel.setFont(Fonts.ui(Font.BOLD, 10f));
        formulaLabel.setForeground(SECONDARY);
        JTextArea formula = new JTextArea(item.formula(), 3, 31);
        formula.setLineWrap(true);
        formula.setWrapStyleWord(true);
        formula.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        formula.setForeground(INK);
        formula.setOpaque(false);
        formula.setBorder(BorderFactory.createEmptyBorder(8, 10, 7, 10));
        JScrollPane editor = new JScrollPane(formula);
        editor.getViewport().setOpaque(false);
        editor.setOpaque(false);
        editor.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));
        editor.setPreferredSize(new Dimension(432, 79));

        JLabel previewLabel = new JLabel("PREVIEW");
        previewLabel.setFont(Fonts.ui(Font.BOLD, 10f));
        previewLabel.setForeground(SECONDARY);
        JLabel preview = new JLabel();
        preview.setHorizontalAlignment(SwingConstants.CENTER);
        preview.setVerticalAlignment(SwingConstants.CENTER);
        JPanel previewCard = new RoundedPreview();
        previewCard.setLayout(new BorderLayout());
        previewCard.add(preview, BorderLayout.CENTER);
        previewCard.setPreferredSize(new Dimension(432, 91));
        JLabel status = new JLabel("US2 will calculate the result when you save.");
        status.setFont(Fonts.ui(Font.PLAIN, 11f));
        status.setForeground(SECONDARY);

        JPanel middle = new JPanel();
        middle.setLayout(new BoxLayout(middle, BoxLayout.Y_AXIS));
        middle.setOpaque(false);
        formulaLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        editor.setAlignmentX(Component.LEFT_ALIGNMENT);
        previewLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        previewCard.setAlignmentX(Component.LEFT_ALIGNMENT);
        status.setAlignmentX(Component.LEFT_ALIGNMENT);
        middle.add(formulaLabel);
        middle.add(Box.createVerticalStrut(6));
        middle.add(editor);
        middle.add(Box.createVerticalStrut(12));
        middle.add(previewLabel);
        middle.add(Box.createVerticalStrut(6));
        middle.add(previewCard);
        middle.add(Box.createVerticalStrut(8));
        middle.add(status);
        surface.add(middle, BorderLayout.CENTER);

        JButton cancel = new CapsuleButton("Cancel", false);
        JButton save = new CapsuleButton("Recalculate & Save", true);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);
        actions.add(cancel);
        actions.add(save);
        surface.add(actions, BorderLayout.SOUTH);

        final Result[] chosen = {null};
        final String[] currentResult = {item.result()};
        final Timer debounce = new Timer(130, e -> {
            String latex = formula.getText().trim();
            if (latex.isBlank()) {
                preview.setIcon(null);
                status.setText("Enter an expression to continue.");
                return;
            }
            try {
                // Manual Entry previews exactly what will be saved, without a computed result.
                String shownResult = modeForSelection(mode.getSelectedIndex()) ==
                        FormulaEditSubmission.Mode.MANUAL ? "" : currentResult[0];
                PdfMaterial sample = item.withText(latex, shownResult);
                preview.setIcon(new ImageIcon(MaterialRenderer.render(sample, 420, 82)));
            } catch (RuntimeException invalid) {
                preview.setIcon(null);
                status.setText("Check the LaTeX syntax.");
            }
        });
        debounce.setRepeats(false);
        Runnable update = () -> {
            currentResult[0] = "";
            status.setForeground(SECONDARY);
            if (modeForSelection(mode.getSelectedIndex()) == FormulaEditSubmission.Mode.MANUAL) {
                save.setText("Save as Entered");
                subtitle.setText("Store the expression without evaluating it.");
                status.setText("No calculation. Type a full equation, e.g. 1+1=3 or i^2=-1.");
            } else {
                save.setText("Recalculate & Save");
                subtitle.setText("Calculate a new result with US2.");
                status.setText("US2 will calculate the result when you save.");
            }
            debounce.restart();
        };
        formula.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { update.run(); }
            public void removeUpdate(DocumentEvent e) { update.run(); }
            public void changedUpdate(DocumentEvent e) { update.run(); }
        });
        mode.addActionListener(e -> update.run());
        debounce.restart();

        cancel.addActionListener(e -> dialog.dispose());
        save.addActionListener(e -> {
            String latex = formula.getText().trim();
            if (latex.isBlank()) {
                status.setForeground(new Color(184, 65, 68));
                status.setText("Formula cannot be empty.");
                return;
            }
            FormulaEditSubmission.Mode selectedMode = modeForSelection(mode.getSelectedIndex());
            if (selectedMode == FormulaEditSubmission.Mode.MANUAL) {
                // Never call US2 in manual mode, even for unsupported expressions.
                FormulaEditSubmission.Revision direct = FormulaEditSubmission.submit(
                        latex, selectedMode, recalculate);
                chosen[0] = new Result(direct.formula(), direct.result());
                dialog.dispose();
                return;
            }
            save.setEnabled(false);
            mode.setEnabled(false);
            formula.setEditable(false);
            status.setForeground(SECONDARY);
            status.setText("Recalculating…");
            // Run the injected evaluator off the Swing event-dispatch thread.
            new SwingWorker<FormulaEditSubmission.Revision, Void>() {
                @Override protected FormulaEditSubmission.Revision doInBackground() {
                    return FormulaEditSubmission.submit(latex, selectedMode, recalculate);
                }
                @Override protected void done() {
                    if (!dialog.isDisplayable()) return;
                    save.setEnabled(true);
                    mode.setEnabled(true);
                    formula.setEditable(true);
                    try {
                        FormulaEditSubmission.Revision computed = get();
                        chosen[0] = new Result(computed.formula(), computed.result());
                        dialog.dispose();
                    } catch (Exception problem) {
                        Throwable cause = problem.getCause() == null ? problem : problem.getCause();
                        status.setForeground(new Color(184, 65, 68));
                        status.setText("Cannot recalculate: " + cause.getMessage() + ". Try Manual Entry.");
                    }
                }
            }.execute();
        });
        dialog.getRootPane().registerKeyboardAction((ActionEvent e) -> dialog.dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        dialog.getRootPane().setDefaultButton(save);
        JPanel frame = new JPanel(new BorderLayout());
        frame.setOpaque(false);
        frame.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        frame.add(surface);
        dialog.setContentPane(frame);
        dialog.pack();
        dialog.setMinimumSize(new Dimension(473, 340));
        dialog.setLocationRelativeTo(owner);
        SwingUtilities.invokeLater(formula::requestFocusInWindow);
        dialog.setVisible(true);
        debounce.stop();
        return chosen[0];
    }

    static FormulaEditSubmission.Mode modeForSelection(int index) {
        return index == 1 ? FormulaEditSubmission.Mode.MANUAL : FormulaEditSubmission.Mode.CALCULATE;
    }

    private static boolean canUseTranslucency() {
        if (GraphicsEnvironment.isHeadless()) return false;
        try {
            return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                    .isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.PERPIXEL_TRANSLUCENT);
        } catch (RuntimeException problem) { return false; }
    }

    private static final class RoundedSurface extends JPanel {
        RoundedSurface() { setOpaque(false); }
        @Override protected void paintComponent(Graphics source) {
            Graphics2D g = (Graphics2D) source.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth() - 1, h = getHeight() - 1;
            g.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 253),
                    0, h, new Color(245, 247, 252, 252)));
            g.fillRoundRect(0, 0, w, h, 24, 24);
            g.setColor(new Color(197, 205, 218));
            g.drawRoundRect(0, 0, w, h, 24, 24);
            g.dispose();
        }
    }
    private static final class RoundedPreview extends JPanel {
        RoundedPreview() { setOpaque(false); }
        @Override protected void paintComponent(Graphics source) {
            Graphics2D g = (Graphics2D) source.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(255, 255, 255, 245));
            g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
            g.setColor(new Color(224, 229, 239));
            g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 14, 14);
            g.dispose();
        }
    }
    private static final class CapsuleButton extends JButton {
        private final boolean primary;
        CapsuleButton(String text, boolean primary) {
            super(text);
            this.primary = primary;
            setFont(Fonts.ui(primary ? Font.BOLD : Font.PLAIN, 12f));
            setForeground(primary ? Color.WHITE : INK);
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(8, 17, 8, 17));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }
        @Override protected void paintComponent(Graphics source) {
            Graphics2D g = (Graphics2D) source.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Color bg = primary ? BLUE : new Color(234, 237, 244);
            if (getModel().isRollover()) bg = primary ? new Color(4, 104, 219) : new Color(225, 231, 243);
            if (!isEnabled()) bg = new Color(171, 188, 210);
            g.setColor(bg);
            g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 18, 18);
            g.dispose();
            super.paintComponent(source);
        }
    }
}
