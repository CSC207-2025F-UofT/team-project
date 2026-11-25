package view.journal;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Predicate;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/** Edits the contents of a single quest's journal page. */
public final class JournalEntryView extends JPanel {
    public static final class Contents {
        public final String entry;
        public final String notes;
        public final BufferedImage photo;

        public Contents(String entry, String notes, BufferedImage photo) {
            this.entry = entry;
            this.notes = notes;
            this.photo = photo;
        }
    }

    private final JournalTextArea entryTextArea;
    private final JournalTextArea notesTextArea;
    private final JLabel photoPreview;
    private BufferedImage photo;

    public JournalEntryView(final String questName, final Contents initialContents,
                            final Predicate<Contents> saveEntry, final Runnable returnToJournal) {
        setLayout(null);
        setBackground(JournalStyle.PAPER);
        setPreferredSize(new Dimension(JournalStyle.WIDTH, JournalStyle.HEIGHT));
        JournalStyle.addPageHeader(this, "Journal Entry",
                () -> navigateBackFromEntry(initialContents, saveEntry, returnToJournal));

        final JLabel questNameLabel = JournalStyle.label(questName, JournalStyle.UI_FONT.deriveFont(14f));
        questNameLabel.setBounds(55, 57, 304, 23);
        questNameLabel.setToolTipText(questName);
        add(questNameLabel);

        final JButton insertPhotoButton = JournalStyle.roundedButton("Insert Photo", JournalStyle.PHOTO_BUTTON,
                this::choosePhoto);
        insertPhotoButton.setBounds(142, 84, 130, 24);
        add(insertPhotoButton);

        photo = initialContents.photo;
        photoPreview = createPhotoPreview();
        add(photoPreview);

        entryTextArea = new JournalTextArea("Journal entry", initialContents.entry,
                "Add a description for your side quest...", 366, 275);
        add(createEntryTextPanel(entryTextArea));

        final int notesTop = 761;
        final int notesHeight = JournalStyle.HEIGHT - notesTop - 8;
        notesTextArea = new JournalTextArea("Notes", initialContents.notes, "", 378, notesHeight);
        notesTextArea.setBounds(15, notesTop, 378, notesHeight);
        add(createNotesLabel(notesTextArea));
        add(notesTextArea);
    }

    private JLabel createPhotoPreview() {
        final JLabel photoPreviewLabel = new JLabel(JournalStyle.loadScaledIcon("polaroid", 168, 196)) {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                if (photo != null) {
                    paintPhoto(graphics);
                }
            }
        };
        photoPreviewLabel.getAccessibleContext().setAccessibleName("Journal photo preview");
        photoPreviewLabel.setBounds(123, 155, 168, 196);
        return photoPreviewLabel;
    }

    private static JPanel createEntryTextPanel(final JournalTextArea entryTextArea) {
        final JPanel entryTextPanel = createRoundedGreenPanel(42);
        entryTextPanel.setBounds(10, 383, 394, 313);
        entryTextArea.setBounds(14, 19, 366, 275);
        entryTextPanel.add(entryTextArea);
        return entryTextPanel;
    }

    private static JLabel createNotesLabel(final JournalTextArea notesTextArea) {
        final JLabel notesLabel = new JLabel("Notes:");
        notesLabel.setFont(JournalStyle.UI_FONT.deriveFont(14f));
        notesLabel.setForeground(JournalStyle.INK);
        notesLabel.setBounds(18, 738, 100, 23);
        notesLabel.setLabelFor(notesTextArea);
        return notesLabel;
    }

    private void choosePhoto() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        FileDialog photoPicker = owner instanceof Dialog
                ? new FileDialog((Dialog) owner, "Insert Photo", FileDialog.LOAD)
                : new FileDialog((Frame) owner, "Insert Photo", FileDialog.LOAD);
        photoPicker.setDirectory(new File(System.getProperty("user.home"), "Downloads").getPath());
        photoPicker.setVisible(true);
        File[] selectedFiles = photoPicker.getFiles();
        photoPicker.dispose();
        if (selectedFiles.length == 0) {
            return;
        }
        try {
            BufferedImage selectedImage = ImageIO.read(selectedFiles[0]);
            if (selectedImage == null) {
                throw new IOException("Unsupported image");
            }
            photo = selectedImage;
            photoPreview.repaint();
        } catch (IOException exception) {
            showPhotoError();
        }
    }

    private void paintPhoto(Graphics graphics) {
        Graphics2D photoGraphics = (Graphics2D) graphics.create();
        int width = 151;
        int height = 144;
        double scale = Math.max((double) width / photo.getWidth(), (double) height / photo.getHeight());
        int scaledWidth = (int) Math.ceil(photo.getWidth() * scale);
        int scaledHeight = (int) Math.ceil(photo.getHeight() * scale);
        photoGraphics.clipRect(8, 9, width, height);
        photoGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        photoGraphics.drawImage(photo, 8 + (width - scaledWidth) / 2, 9 + (height - scaledHeight) / 2,
                scaledWidth, scaledHeight, null);
        photoGraphics.dispose();
    }

    private void navigateBackFromEntry(Contents initialContents, Predicate<Contents> saveEntry,
                                       Runnable returnToJournal) {
        Contents editedContents = new Contents(entryTextArea.getText(), notesTextArea.getText(), photo);
        if (editedContents.entry.equals(initialContents.entry) && editedContents.notes.equals(initialContents.notes)
                && editedContents.photo == initialContents.photo) {
            returnToJournal.run();
            return;
        }

        JDialog promptDialog = createModalDialog("Save your entry?");
        JPanel content = createRoundedGreenPanel(36);
        content.setPreferredSize(new Dimension(360, 148));
        JLabel saveQuestionLabel = JournalStyle.label("Save your entry?", JournalStyle.UI_FONT.deriveFont(15f));
        saveQuestionLabel.setBounds(0, 18, 360, 31);
        content.add(saveQuestionLabel);

        JButton saveButton = JournalStyle.roundedButton("Save", JournalStyle.PAPER, () -> {
            promptDialog.dispose();
            if (saveEntry.test(editedContents)) {
                returnToJournal.run();
            }
        });
        JButton discardButton = JournalStyle.roundedButton("Don't Save", JournalStyle.PAPER, () -> {
            promptDialog.dispose();
            returnToJournal.run();
        });
        saveButton.setBounds(12, 60, 159, 47);
        discardButton.setBounds(179, 60, 161, 47);
        content.add(saveButton);
        content.add(discardButton);
        showDialog(promptDialog, content, saveButton);
    }

    private void showPhotoError() {
        JDialog promptDialog = createModalDialog("Photo could not be opened");
        JPanel content = createRoundedGreenPanel(36);
        content.setPreferredSize(new Dimension(360, 110));
        JLabel message = JournalStyle.label("Choose a readable image, such as a JPEG or PNG.",
                JournalStyle.UI_FONT.deriveFont(13f));
        message.setBounds(0, 14, 360, 30);
        content.add(message);
        JButton dismissButton = JournalStyle.roundedButton("OK", JournalStyle.PAPER, promptDialog::dispose);
        dismissButton.setBounds(134, 57, 92, 33);
        content.add(dismissButton);
        showDialog(promptDialog, content, dismissButton);
    }

    private JDialog createModalDialog(String dialogTitle) {
        JDialog modalDialog = new JDialog(SwingUtilities.getWindowAncestor(this), dialogTitle,
                Dialog.ModalityType.APPLICATION_MODAL);
        modalDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        return modalDialog;
    }

    private void showDialog(JDialog modalDialog, JPanel content, JButton defaultButton) {
        modalDialog.setContentPane(content);
        modalDialog.getRootPane().setDefaultButton(defaultButton);
        modalDialog.pack();
        modalDialog.setLocationRelativeTo(this);
        modalDialog.setVisible(true);
    }

    private static JPanel createRoundedGreenPanel(int arc) {
        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics graphics) {
                JournalStyle.paintRounded(graphics, JournalStyle.GREEN, getWidth(), getHeight(), arc);
            }
        };
        panel.setOpaque(false);
        return panel;
    }
}
