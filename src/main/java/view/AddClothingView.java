package view;

import interface_adapter.add_clothing.AddClothingController;
import interface_adapter.add_clothing.AddClothingState;
import interface_adapter.add_clothing.AddClothingViewModel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The Add Clothing form: name, category, type, colour, style, layer (yes/no),
 * the temperatures it can be worn in (a two-handle range slider) and a picture,
 * with Save, Cancel, Draft and Home buttons. Says which fields are required.
 * Only shows things and passes clicks to the Controller; no logic here.
 */
public class AddClothingView extends JPanel implements PropertyChangeListener {
    private static final Color ERROR_RED = new Color(0xB4, 0x23, 0x18);
    private static final Color SUCCESS_GREEN = new Color(0x0F, 0x5A, 0x2C);
    private static final Color MUTED = new Color(0x4A, 0x4F, 0x5C);
    private static final int THUMBNAIL = 96;

    private final AddClothingViewModel viewModel;
    private AddClothingController controller;

    private final JTextField nameField = new JTextField(18);
    private final JComboBox<String> categoryBox = new JComboBox<>();
    private final JComboBox<String> subCategoryBox = new JComboBox<>();
    private final JComboBox<String> colorBox = new JComboBox<>();
    private final JComboBox<String> styleBox = new JComboBox<>();
    private final JRadioButton layerYes = new JRadioButton("Yes");
    private final JRadioButton layerNo = new JRadioButton("No", true);
    private final RangeSlider weatherSlider;
    private final JLabel weatherRangeLabel = new JLabel(" ");

    private final JLabel thumbnail = new JLabel("No picture", SwingConstants.CENTER);
    private final JLabel fileNameLabel = new JLabel("No picture chosen");
    private final JButton choosePictureButton = new JButton("Choose Picture…");
    private String chosenImagePath = "";

    private final JLabel errorLabel = new JLabel(" ");
    private final JLabel messageLabel = new JLabel(" ");

    private final JButton homeButton = new JButton("Home");
    private final JButton cancelButton = new JButton("Cancel");
    private final JButton draftButton = new JButton("Draft");
    private final JButton saveButton = new JButton("Save");

    public AddClothingView(AddClothingViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.addPropertyChangeListener(this);
        this.weatherSlider = new RangeSlider(viewModel.getWeatherLabels());

        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));

        add(buildHeader(), BorderLayout.NORTH);
        add(buildForm(), BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        fillDropdowns();
        hookUpButtons();
    }

    public void setAddClothingController(AddClothingController controller) {
        this.controller = controller;
    }

    public String getViewName() {
        return viewModel.getViewName();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final AddClothingState state = viewModel.getState();
        switch (evt.getPropertyName()) {
            case "error":
                errorLabel.setText(state.getError());
                messageLabel.setText(" ");
                break;
            case "success":
                clearForm();
                errorLabel.setText(" ");
                messageLabel.setText(state.getMessage());
                break;
            case "reset":
                clearForm();
                errorLabel.setText(" ");
                messageLabel.setText(" ");
                break;
            default:
                break;
        }
    }


    private JPanel buildHeader() {
        final JPanel header = new JPanel(new BorderLayout());
        final JLabel title = new JLabel("Add Clothing");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        header.add(title, BorderLayout.WEST);
        header.add(homeButton, BorderLayout.EAST);

        final JLabel requiredNote = new JLabel("Fields marked * are required.");
        requiredNote.setForeground(MUTED);
        requiredNote.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        header.add(requiredNote, BorderLayout.SOUTH);
        return header;
    }

    private JPanel buildForm() {
        final JPanel form = new JPanel(new GridBagLayout());
        int row = 0;
        addRow(form, row++, "Name *", nameField);
        addRow(form, row++, "Category *", categoryBox);
        addRow(form, row++, "Type *", subCategoryBox);
        addRow(form, row++, "Colour *", colorBox);
        addRow(form, row++, "Style *", styleBox);
        addRow(form, row++, "Layer", buildLayerChoice());
        addRow(form, row++, "Weather *", buildWeatherSlider());
        addRow(form, row++, "Picture *", buildPicturePicker());

        final GridBagConstraints messages = constraints(0, row);
        messages.gridwidth = 2;
        final JPanel messagePanel = new JPanel(new BorderLayout());
        errorLabel.setForeground(ERROR_RED);
        messageLabel.setForeground(SUCCESS_GREEN);
        messagePanel.add(errorLabel, BorderLayout.NORTH);
        messagePanel.add(messageLabel, BorderLayout.SOUTH);
        form.add(messagePanel, messages);
        return form;
    }

    private JPanel buildLayerChoice() {
        final ButtonGroup group = new ButtonGroup();
        group.add(layerYes);
        group.add(layerNo);
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.add(layerYes);
        panel.add(layerNo);
        return panel;
    }

    private JPanel buildWeatherSlider() {
        weatherRangeLabel.setForeground(MUTED);
        weatherSlider.addChangeListener(e -> weatherRangeLabel.setText(
                viewModel.describeRange(weatherSlider.getLow(), weatherSlider.getHigh())));
        weatherSlider.setRange(0, Integer.MAX_VALUE);

        final JPanel panel = new JPanel(new BorderLayout(0, 4));
        panel.add(weatherSlider, BorderLayout.CENTER);
        panel.add(weatherRangeLabel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildPicturePicker() {
        thumbnail.setPreferredSize(new Dimension(THUMBNAIL, THUMBNAIL));
        thumbnail.setBorder(BorderFactory.createDashedBorder(MUTED));
        fileNameLabel.setForeground(MUTED);

        final JPanel right = new JPanel(new BorderLayout(0, 6));
        right.add(choosePictureButton, BorderLayout.NORTH);
        right.add(fileNameLabel, BorderLayout.CENTER);

        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        panel.add(thumbnail);
        panel.add(right);
        return panel;
    }

    private JPanel buildFooter() {
        draftButton.setEnabled(false);
        draftButton.setToolTipText("Coming soon");
        final JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        footer.add(cancelButton);
        footer.add(draftButton);
        footer.add(saveButton);
        return footer;
    }

    private static void addRow(JPanel form, int row, String label, JComponent field) {
        final JLabel fieldLabel = new JLabel(label);
        fieldLabel.setLabelFor(field);
        form.add(fieldLabel, constraints(0, row));
        final GridBagConstraints right = constraints(1, row);
        right.weightx = 1;
        right.fill = GridBagConstraints.HORIZONTAL;
        form.add(field, right);
    }

    private static GridBagConstraints constraints(int column, int row) {
        final GridBagConstraints c = new GridBagConstraints();
        c.gridx = column;
        c.gridy = row;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(5, 0, 5, 12);
        return c;
    }

    private void fillDropdowns() {
        setChoices(categoryBox, viewModel.getCategoryChoices());
        setChoices(subCategoryBox, viewModel.getSubCategoryChoices(""));
        setChoices(colorBox, viewModel.getColorChoices());
        setChoices(styleBox, viewModel.getStyleChoices());
        categoryBox.addActionListener(e -> setChoices(subCategoryBox,
                viewModel.getSubCategoryChoices((String) categoryBox.getSelectedItem())));
    }

    private void hookUpButtons() {
        choosePictureButton.addActionListener(e -> choosePicture());
        saveButton.addActionListener(e -> controller.execute(
                nameField.getText(),
                (String) categoryBox.getSelectedItem(),
                (String) subCategoryBox.getSelectedItem(),
                (String) colorBox.getSelectedItem(),
                (String) styleBox.getSelectedItem(),
                layerYes.isSelected(),
                weatherSlider.getLow(),
                weatherSlider.getHigh(),
                chosenImagePath));
        cancelButton.addActionListener(e -> controller.switchToWelcomeView());
        homeButton.addActionListener(e -> controller.switchToWelcomeView());
    }

    private void choosePicture() {
        final JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Pictures (png, jpg)", "png", "jpg", "jpeg"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            final File file = chooser.getSelectedFile();
            chosenImagePath = file.getAbsolutePath();
            fileNameLabel.setText(file.getName());
            showThumbnail(chosenImagePath);
        }
    }

    private void showThumbnail(String path) {
        final ImageIcon icon = new ImageIcon(path);
        if (icon.getIconWidth() <= 0) {
            thumbnail.setIcon(null);
            thumbnail.setText("Can't preview");
            return;
        }
        final Image scaled = icon.getImage().getScaledInstance(THUMBNAIL, THUMBNAIL, Image.SCALE_SMOOTH);
        thumbnail.setText("");
        thumbnail.setIcon(new ImageIcon(scaled));
    }

    private void clearForm() {
        nameField.setText("");
        categoryBox.setSelectedIndex(0);
        colorBox.setSelectedIndex(0);
        styleBox.setSelectedIndex(0);
        layerNo.setSelected(true);
        weatherSlider.setRange(0, Integer.MAX_VALUE);
        chosenImagePath = "";
        fileNameLabel.setText("No picture chosen");
        thumbnail.setIcon(null);
        thumbnail.setText("No picture");
    }

    private static void setChoices(JComboBox<String> box, List<String> choices) {
        box.setModel(new DefaultComboBoxModel<>(choices.toArray(new String[0])));
    }
}
