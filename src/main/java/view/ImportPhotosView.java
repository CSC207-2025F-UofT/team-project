package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import interface_adapter.import_photos.ImportPhotosController;
import interface_adapter.import_photos.ImportPhotosStage;
import interface_adapter.import_photos.ImportPhotosState;
import interface_adapter.import_photos.ImportPhotosViewModel;
import use_case.import_photos.ImportPhotosGeocodeOutputData;
import use_case.import_photos.ImportPhotosImportOutputData;
import use_case.import_photos.ImportPhotosTripOutputData;

public class ImportPhotosView extends JPanel implements PropertyChangeListener {
    private static final String CARD_INPUT = "input";
    private static final String CARD_PROGRESS = "progress";

    private static final String SUB_CARD_EMPTY = "empty";
    private static final String SUB_CARD_ERROR = "error";
    private static final String SUB_CARD_LABEL = "label";
    private static final String SUB_CARD_PROGRESS = "progress";
    private static final String SUB_CARD_REPORT = "report";

    private static final String NO_ITEMS_MESSAGE = "No items selected";
    private static final String ITEMS_MESSAGE = "%d item%s selected";

    private static final Map<ImportPhotosStage, String> PROGRESS_MESSAGES = Map.of(
        ImportPhotosStage.PHOTO_SAVING, "Saving photos...",
        ImportPhotosStage.PHOTO_SORTING, "Sorting photos...",
        ImportPhotosStage.TRIP_GENERATION, "Generating trips...",
        ImportPhotosStage.TRIP_SAVING, "Saving trips..."
    );

    private static final String IMPORT_PROGRESS_STRING = "Importing: %d / %d";
    private static final String GEOCODE_PROGRESS_STRING = "Reverse Geocoding: %d / %d";

    private final ImportPhotosViewModel viewModel;
    private final ImportPhotosController controller;

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    private final CardLayout progressCardLayout = new CardLayout();
    private final JPanel progressCardPanel = new JPanel(progressCardLayout);

    private File[] selectedFiles;

    private final JLabel errorLabel = new JLabel();
    private final JLabel progressLabel = new JLabel();
    private final JProgressBar progressBar = new JProgressBar();
    private final JTextArea reportArea = new JTextArea();
    private final JScrollPane reportScrollPane = new JScrollPane(reportArea);
    private final JButton backButton = new JButton("Go Back");
    private final JPanel backPanel = new JPanel();

    public ImportPhotosView(ImportPhotosViewModel viewModel, ImportPhotosController controller) {
        this.viewModel = viewModel;
        this.controller = controller;

        viewModel.addPropertyChangeListener(this);

        cardPanel.add(buildInputPanel(), CARD_INPUT);
        cardPanel.add(buildProgressPanel(), CARD_PROGRESS);
        cardLayout.show(cardPanel, CARD_INPUT);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        add(cardPanel, BorderLayout.CENTER);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        SwingUtilities.invokeLater(this::refreshProgress);
    }

    private JPanel buildFieldPanel(JTextField field, String label, String hint) {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 4, 0);
        panel.add(new JLabel(label), gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 4, 0);
        panel.add(field, gbc);

        gbc.gridy = 2;
        panel.add(new JLabel(hint), gbc);

        return panel;
    }

    private JPanel buildInputPanel() {
        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new FileNameExtensionFilter(
            "Images (JPG, PNG, WEBP)", "jpg", "jpeg", "png", "webp")
        );

        final JButton pickButton = new JButton("Choose Files/Folders");
        pickButton.setFocusPainted(false);

        final JLabel filesLabel = new JLabel(NO_ITEMS_MESSAGE);

        final JTextField locationField = new JTextField(28);
        final JPanel locationFieldPanel = buildFieldPanel(
            locationField,
            "Exclusion Location",
            "Optional place name to exclude nearby photos from automatic trip grouping."
        );

        final JTextField distanceField = new JTextField(12);
        final JPanel distanceFieldPanel = buildFieldPanel(
            distanceField,
            "Exclusion Distance (km)",
            "Distance around the exclusion location to ignore when a location is specified."
        );

        final JButton submitButton = new JButton("Start Import");
        submitButton.setFocusPainted(false);

        pickButton.addActionListener(e -> {
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                File[] newSelectedFiles = fileChooser.getSelectedFiles();

                if (newSelectedFiles != null && newSelectedFiles.length > 0) {
                    selectedFiles = newSelectedFiles;
                    filesLabel.setText(ITEMS_MESSAGE.formatted(
                        selectedFiles.length,
                        selectedFiles.length > 1 ? "s" : ""
                    ));
                } else {
                    selectedFiles = null;
                    filesLabel.setText(NO_ITEMS_MESSAGE);
                }
            }
        });

        submitButton.addActionListener(ev -> {
            String location = locationField.getText();
            int distance = -1;

            if (location == null || location.length() == 0) {
                location = null;
            } else {
                try {
                    distance = Integer.parseInt(distanceField.getText());
                } catch (NumberFormatException e) {
                    return;
                }
            }

            if (selectedFiles != null) {
                File[] files = selectedFiles;

                locationField.setText("");
                distanceField.setText("");
                selectedFiles = null;
                filesLabel.setText(NO_ITEMS_MESSAGE);

                progressCardLayout.show(progressCardPanel, SUB_CARD_EMPTY);
                cardLayout.show(cardPanel, CARD_PROGRESS);

                controller.execute(location, distance, files);
            }
        });

        final JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;

        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 6, 0);
        centerPanel.add(pickButton, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 32, 0);
        centerPanel.add(filesLabel, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 32, 0);
        centerPanel.add(locationFieldPanel, gbc);

        gbc.gridy = 3;
        centerPanel.add(distanceFieldPanel, gbc);

        final JPanel bottomPanel = new JPanel();
        bottomPanel.add(submitButton);

        final JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildProgressPanel() {
        final JPanel progressCard = new JPanel();
        progressCard.setLayout(new BorderLayout());

        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setOpaque(true);

        progressLabel.setHorizontalAlignment(SwingConstants.CENTER);

        progressBar.setMinimum(0);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(420, 28));

        final JPanel progressBarPanel = new JPanel(new GridBagLayout());
        progressBarPanel.setOpaque(false);
        progressBarPanel.add(progressBar);

        reportArea.setOpaque(false);
        reportArea.setBorder(BorderFactory.createEmptyBorder());
        reportArea.setEditable(false);
        reportArea.setLineWrap(true);
        reportArea.setWrapStyleWord(true);
        reportArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

        reportScrollPane.setBorder(BorderFactory.createEmptyBorder());

        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> cardLayout.show(cardPanel, CARD_INPUT));

        backPanel.setOpaque(false);
        backPanel.setVisible(false);
        backPanel.add(backButton);

        final JPanel emptyPanel = new JPanel();
        emptyPanel.setOpaque(false);

        progressCardPanel.setOpaque(false);
        progressCardPanel.add(emptyPanel, SUB_CARD_EMPTY);
        progressCardPanel.add(errorLabel, SUB_CARD_ERROR);
        progressCardPanel.add(progressLabel, SUB_CARD_LABEL);
        progressCardPanel.add(progressBarPanel, SUB_CARD_PROGRESS);
        progressCardPanel.add(reportScrollPane, SUB_CARD_REPORT);
        progressCardLayout.show(progressCardPanel, SUB_CARD_EMPTY);

        progressCard.add(progressCardPanel, BorderLayout.CENTER);

        final JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.add(progressCard, BorderLayout.CENTER);
        panel.add(backPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void setBackVisible(boolean visible) {
        backPanel.setVisible(visible);
    }

    private void updateProgressBar(int value, int maximum, String string) {
        progressBar.setMaximum(maximum);
        progressBar.setValue(value);
        progressBar.setString(string);
        progressCardLayout.show(progressCardPanel, SUB_CARD_PROGRESS);
        setBackVisible(false);
    }

    private void generateReport() {
        ImportPhotosState state = viewModel.getState();
        ImportPhotosImportOutputData importData = state.getImportData();
        ImportPhotosGeocodeOutputData geocodeData = state.getGeocodeData();
        ImportPhotosTripOutputData tripData = state.getTripData();

        StringBuilder sb = new StringBuilder();

        if (importData != null) {
            List<String> failedPaths = importData.getFailedPaths();

            sb.append("Importing:\n");
            sb.append("    Imported: %d / %d\n".formatted(importData.getSuccesses(), importData.getTotal()));
            sb.append("    Failed: %d\n".formatted(failedPaths.size()));

            if (!failedPaths.isEmpty()) {
                sb.append("    Failed Paths:\n");

                for (String path : importData.getFailedPaths()) {
                    sb.append("        ").append(path).append("\n");
                }
            }

            sb.append("\n");
        }

        if (geocodeData != null) {
            sb.append("Reverse Geocoding:\n");
            sb.append("    Reverse Geocoded: %d\n".formatted(geocodeData.getGeocodedCount()));
            sb.append("    Skipped: %d\n".formatted(geocodeData.getSkipCount()));
            sb.append("    Failed: %d\n".formatted(geocodeData.getFailCount()));
            sb.append("\n");
        }

        if (state.getStage() == ImportPhotosStage.TRIP_SAVE_FAILURE) {
            sb.append("Trips Generated: None\n");
            sb.append("    (Failed to save trips)");

        } else if (tripData != null) {
            List<String> tripNames = tripData.getTripNames();

            sb.append("Trip Generation:\n");
            sb.append("    Trips Generated: %d\n".formatted(tripNames.size()));
            sb.append("    Photos Sorted Into Trips: %d\n".formatted(tripData.getSortedPhotoCount()));

            if (!tripNames.isEmpty()) {
                sb.append("    Trip Names:\n");

                for (String name : tripNames) {
                    sb.append("        ").append(name).append("\n");
                }
            }
        }

        reportArea.setText(sb.toString());
        reportArea.setCaretPosition(0);
        progressCardLayout.show(progressCardPanel, SUB_CARD_REPORT);
        setBackVisible(true);
    }

    private void refreshProgress() {
        ImportPhotosState state = viewModel.getState();
        ImportPhotosStage stage = state.getStage();
        ImportPhotosImportOutputData importData = state.getImportData();
        ImportPhotosGeocodeOutputData geocodeData = state.getGeocodeData();

        switch (stage) {
            case INVALID_EXCLUSION, IMPORT_FAILURE, PHOTO_SAVE_FAILURE -> {
                errorLabel.setText(state.getStageError());
                progressCardLayout.show(progressCardPanel, SUB_CARD_ERROR);
                setBackVisible(true);
            }
            case PHOTO_SAVING, PHOTO_SORTING, TRIP_GENERATION, TRIP_SAVING -> {
                progressLabel.setText(PROGRESS_MESSAGES.get(stage));
                progressCardLayout.show(progressCardPanel, SUB_CARD_LABEL);
                setBackVisible(false);
            }
            case IMPORTING -> {
                updateProgressBar(
                    importData.getSuccesses() + importData.getFailedPaths().size(),
                    importData.getTotal(),
                    IMPORT_PROGRESS_STRING.formatted(
                        importData.getSuccesses() + importData.getFailedPaths().size(),
                        importData.getTotal()
                    )
                );
            }
            case GEOCODING -> {
                updateProgressBar(
                    geocodeData.getGeocodedCount() + geocodeData.getFailCount() + geocodeData.getSkipCount(),
                    importData.getTotal(),
                    GEOCODE_PROGRESS_STRING.formatted(
                        geocodeData.getGeocodedCount() + geocodeData.getFailCount() + geocodeData.getSkipCount(),
                        importData.getTotal()
                    )
                );
            }
            case TRIP_SAVE_FAILURE, TRIP_SAVE_SUCCESS -> {
                generateReport();
            }
        }
    }
}
