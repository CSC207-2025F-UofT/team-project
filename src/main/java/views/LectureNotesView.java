package views;

import interface_adapters.lecturenotes.GenerateLectureNotesController;
import interface_adapters.lecturenotes.LectureNotesViewModel;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class LectureNotesView extends JPanel {

    private final transient GenerateLectureNotesController controller;
    private final transient LectureNotesViewModel viewModel;

    private final JTextField topicField = new JTextField();
    private final JTextArea notesArea = new JTextArea();
    private final JLabel errorLabel = new JLabel();
    private final JButton generateButton = new JButton("Generate Notes");

    private transient Highlighter highlighter;
    private final transient Highlighter.HighlightPainter highlightPainter =
            new DefaultHighlighter.DefaultHighlightPainter(new Color(255, 255, 120));

    private final JButton highlightButton = new JButton("Highlight");
    private final JButton clearHighlightsButton = new JButton("Clear Highlights");
    private final JButton saveButton = new JButton("Save");
    private final JButton copyButton = new JButton("Copy");
    private final JButton clearTextButton = new JButton("Clear");

    public LectureNotesView(GenerateLectureNotesController controller,
                            LectureNotesViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // Top: topic + generate
        JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Topic: "), BorderLayout.WEST);
        top.add(topicField, BorderLayout.CENTER);

        generateButton.addActionListener(this::onGenerateClicked);
        top.add(generateButton, BorderLayout.EAST);

        add(top, BorderLayout.NORTH);

        // Center: notes area
        notesArea.setEditable(true);
        notesArea.setLineWrap(true);
        notesArea.setWrapStyleWord(true);

        notesArea.setHighlighter(new DefaultHighlighter());
        highlighter = notesArea.getHighlighter();

        JScrollPane scrollPane = new JScrollPane(notesArea);
        scrollPane.setHorizontalScrollBarPolicy(
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom: error + buttons
        errorLabel.setForeground(Color.RED);

        highlightButton.addActionListener(e -> onHighlightClicked());
        clearHighlightsButton.addActionListener(e -> onClearHighlightsClicked());
        saveButton.addActionListener(e -> onSaveClicked());
        copyButton.addActionListener(e -> onCopyClicked());
        clearTextButton.addActionListener(e -> onClearTextClicked());

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(errorLabel, BorderLayout.WEST);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(highlightButton);
        buttonsPanel.add(clearHighlightsButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(copyButton);
        buttonsPanel.add(clearTextButton);

        bottom.add(buttonsPanel, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);
    }

    private void onGenerateClicked(ActionEvent e) {
        String dummyCourseId = "";
        List<String> dummyFilePaths = List.of();
        String topic = topicField.getText().trim();

        if (topic.isEmpty()) {
            errorLabel.setText("Topic cannot be empty.");
            return;
        }

        generateButton.setEnabled(false);
        notesArea.setEditable(false);
        notesArea.setText("Generating notes, please wait...");
        errorLabel.setText("");

        if (highlighter != null) {
            highlighter.removeAllHighlights();
        }

        new Thread(() -> {
            controller.generate(dummyCourseId, dummyFilePaths, topic);

            SwingUtilities.invokeLater(() -> {
                notesArea.setText(viewModel.getNotesText());
                notesArea.setEditable(true);
                errorLabel.setText(viewModel.getErrorMessage());
                generateButton.setEnabled(true);
            });
        }).start();
    }

    // ===== Highlight in UI =====

    private void onHighlightClicked() {
        int start = notesArea.getSelectionStart();
        int end = notesArea.getSelectionEnd();
        if (start == end) {
            errorLabel.setText("Select some text to highlight.");
            return;
        }
        try {
            highlighter.addHighlight(start, end, highlightPainter);
            errorLabel.setText("");
        } catch (BadLocationException ex) {
            ex.printStackTrace();
            errorLabel.setText("Could not apply highlight.");
        }
    }

    private void onClearHighlightsClicked() {
        if (highlighter != null) {
            highlighter.removeAllHighlights();
        }
        errorLabel.setText("");
    }

    // ===== Save as HTML with <mark> highlights =====

    private void onSaveClicked() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save notes as HTML");
        int result = chooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();
        String name = file.getName().toLowerCase();
        if (!name.endsWith(".html") && !name.endsWith(".htm")) {
            file = new File(file.getParentFile(), file.getName() + ".html");
        }

        String text = notesArea.getText();
        String html = buildHtmlDocument(text);

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(html);
            errorLabel.setText("Saved to " + file.getName());
        } catch (IOException ex) {
            ex.printStackTrace();
            errorLabel.setText("Failed to save notes.");
        }
    }

    // ===== Copy & Clear =====

    private void onCopyClicked() {
        String text = notesArea.getText();
        if (text == null || text.isBlank()) {
            errorLabel.setText("Nothing to copy.");
            return;
        }
        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
        errorLabel.setText("Copied notes to clipboard.");
    }

    private void onClearTextClicked() {
        notesArea.setText("");
        if (highlighter != null) {
            highlighter.removeAllHighlights();
        }
        errorLabel.setText("");
    }

    // ===== HTML helpers =====

    private static class HighlightRange {
        int start;
        int end;

        HighlightRange(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }

    /** Build full HTML document with <mark> tags for highlighted ranges. */
    private String buildHtmlDocument(String text) {
        String title = topicField.getText().trim();
        if (title.isEmpty()) {
            title = "Lecture Notes";
        }

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html><head><meta charset=\"UTF-8\"><title>");
        html.append(escapeHtml(title));
        html.append("</title></head><body>");
        html.append("<pre style=\"font-family: sans-serif; white-space: pre-wrap;\">");
        html.append(buildHtmlBody(text));
        html.append("</pre></body></html>");
        return html.toString();
    }

    /** Insert <mark>...</mark> around highlighted ranges. */
    private String buildHtmlBody(String text) {
        List<HighlightRange> ranges = collectHighlightRanges(text.length());
        StringBuilder body = new StringBuilder();
        int pos = 0;

        for (HighlightRange r : ranges) {
            if (r.start > pos) {
                body.append(escapeHtml(text.substring(pos, r.start)));
            }
            body.append("<mark>");
            body.append(escapeHtml(text.substring(r.start, r.end)));
            body.append("</mark>");
            pos = r.end;
        }

        if (pos < text.length()) {
            body.append(escapeHtml(text.substring(pos)));
        }

        return body.toString();
    }

    /** Collect and merge highlight ranges from the Swing Highlighter. */
    private List<HighlightRange> collectHighlightRanges(int textLength) {
        List<HighlightRange> ranges = new ArrayList<>();
        if (highlighter == null) {
            return ranges;
        }

        for (Highlighter.Highlight h : highlighter.getHighlights()) {
            int start = Math.max(0, h.getStartOffset());
            int end = Math.min(textLength, h.getEndOffset());
            if (start < end) {
                ranges.add(new HighlightRange(start, end));
            }
        }

        ranges.sort(Comparator.comparingInt(r -> r.start));

        if (ranges.isEmpty()) {
            return ranges;
        }

        // merge overlapping ranges
        List<HighlightRange> merged = new ArrayList<>();
        HighlightRange current = ranges.get(0);

        for (int i = 1; i < ranges.size(); i++) {
            HighlightRange next = ranges.get(i);
            if (next.start <= current.end) {
                current.end = Math.max(current.end, next.end);
            } else {
                merged.add(current);
                current = next;
            }
        }
        merged.add(current);
        return merged;
    }

    private String escapeHtml(String s) {
        if (s == null || s.isEmpty()) {
            return "";
        }
        String result = s;
        result = result.replace("&", "&amp;");
        result = result.replace("<", "&lt;");
        result = result.replace(">", "&gt;");
        result = result.replace("\"", "&quot;");
        return result;
    }
}
