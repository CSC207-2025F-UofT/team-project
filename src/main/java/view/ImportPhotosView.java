package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import interface_adapter.import_photos.ImportPhotosViewModel;
import use_case.import_photos.ImportPhotosGeocodeOutputData;
import use_case.import_photos.ImportPhotosImportOutputData;
import use_case.import_photos.ImportPhotosTripOutputData;
import interface_adapter.import_photos.ImportPhotosController;
import interface_adapter.import_photos.ImportPhotosStage;
import interface_adapter.import_photos.ImportPhotosState;

public class ImportPhotosView extends JPanel implements PropertyChangeListener {
    private static final String CARD_INPUT = "input";
    private static final String CARD_PROGRESS = "progress";

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

    private static final String IMPORT_PROGRESS_STRING = "Importing: (%d + %d) / %d";
    private static final String GEOCODE_PROGRESS_STRING = "Geocoding: (%d + %d + %d) / %d";

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

    public ImportPhotosView(ImportPhotosViewModel viewModel, ImportPhotosController controller) {
        this.viewModel = viewModel;
        this.controller = controller;

        viewModel.addPropertyChangeListener(this);

        cardPanel.add(buildInputPanel(), CARD_INPUT);
        cardPanel.add(buildProgressPanel(), CARD_PROGRESS);
        cardLayout.show(cardPanel, CARD_INPUT);


        setLayout(new BorderLayout());
        add(cardPanel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        SwingUtilities.invokeLater(this::refreshProgress);
    }

    private JPanel buildInputPanel() {
        final JPanel panel = new JPanel();

        final JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setMultiSelectionEnabled(true);
        fileChooser.setFileFilter(new FileNameExtensionFilter(
            "Images (JPG, PNG, WEBP)", "jpg", "jpeg", "png", "webp")
        );

        final JButton pickButton = new JButton("Choose Files/Folders");
        final JLabel filesLabel = new JLabel("No items selected");

        final JTextField locationField = new JTextField(30);
        final JTextField distanceField = new JTextField(10);

        final JButton submitButton = new JButton("Start Import");

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
                cardLayout.show(cardPanel, CARD_PROGRESS);

                controller.execute(location, distance, files);
            }
        });

        panel.add(pickButton);
        panel.add(new JLabel("Exclusion Location:"));
        panel.add(locationField);
        panel.add(new JLabel("Exclusion Distance (km):"));
        panel.add(distanceField);
        panel.add(submitButton);

        return panel;
    }

    private JPanel buildProgressPanel() {
        JPanel panel = new JPanel();

        progressBar.setMinimum(0);
        progressBar.setStringPainted(true);

        reportArea.setEditable(false);
        reportArea.setLineWrap(true);
        reportArea.setWrapStyleWord(true);

        backButton.setVisible(false);
        backButton.addActionListener(e -> {
            cardLayout.show(cardPanel, CARD_INPUT);
        });

        progressCardPanel.add(errorLabel, SUB_CARD_ERROR);
        progressCardPanel.add(progressLabel, SUB_CARD_LABEL);
        progressCardPanel.add(progressBar, SUB_CARD_PROGRESS);
        progressCardPanel.add(reportScrollPane, SUB_CARD_REPORT);

        panel.setLayout(new BorderLayout());
        panel.add(progressCardPanel, BorderLayout.CENTER);
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }

    private void updateProgressBar(int value, int maximum, String string) {
        progressBar.setMaximum(maximum);
        progressBar.setValue(value);
        progressBar.setString(string);
        progressCardLayout.show(progressCardPanel, SUB_CARD_PROGRESS);
        backButton.setVisible(false);
    }

    private void generateReport() {
        reportArea.setText("Test");
        progressCardLayout.show(progressCardPanel, SUB_CARD_REPORT);
        backButton.setVisible(false);
    }

    private void refreshProgress() {
        ImportPhotosState state = viewModel.getState();
        ImportPhotosStage stage = state.getStage();
        ImportPhotosImportOutputData importData = state.getImportData();
        ImportPhotosGeocodeOutputData geocodeData = state.getGeocodeData();
        ImportPhotosTripOutputData tripData = state.getTripData();

        switch (stage) {
            case INVALID_EXCLUSION, IMPORT_FAILURE, PHOTO_SAVE_FAILURE -> {
                errorLabel.setText(state.getStageError());
                progressCardLayout.show(progressCardPanel, SUB_CARD_ERROR);
                backButton.setVisible(true);
            }
            case PHOTO_SAVING, PHOTO_SORTING, TRIP_GENERATION, TRIP_SAVING -> {
                progressLabel.setText(PROGRESS_MESSAGES.get(stage));
                progressCardLayout.show(progressCardPanel, SUB_CARD_LABEL);
                backButton.setVisible(false);
            }
            case IMPORTING -> {
                updateProgressBar(
                    importData.getSuccesses() + importData.getFailedPaths().size(),
                    importData.getTotal(),
                    IMPORT_PROGRESS_STRING.formatted(
                        importData.getSuccesses(),
                        importData.getFailedPaths().size(),
                        importData.getTotal()
                    )
                );
            }
            case GEOCODING -> {
                updateProgressBar(
                    geocodeData.getGeocodedCount() + geocodeData.getFailCount() + geocodeData.getSkipCount(),
                    importData.getTotal(),
                    GEOCODE_PROGRESS_STRING.formatted(
                        geocodeData.getGeocodedCount(),
                        geocodeData.getFailCount(),
                        geocodeData.getSkipCount(),
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
