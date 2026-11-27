package view;

import entity.Library;
import interface_adapter.save_result.SaveResultController;
import interface_adapter.save_result.SaveResultViewModel;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Name box + Save button shown after the quiz. Redraws when the view model changes.
 */
public class SaveResultView extends JPanel implements PropertyChangeListener {
    private final SaveResultViewModel viewModel;

    private final JTextField nameField = new JTextField(15);
    private final JButton saveButton = new JButton("Save");
    private final JLabel messageLabel = new JLabel(" ");

    // Set by take quiz before the user presses Save
    private List<Integer> chosenIndices = List.of();
    private Library matchedLibrary;

    public SaveResultView(SaveResultViewModel viewModel, SaveResultController controller) {
        this.viewModel = viewModel;
        viewModel.addPropertyChangeListener(this);

        JPanel namePanel = new JPanel();
        namePanel.add(new JLabel("Name:"));
        namePanel.add(nameField);

        // Save button -> controller
        saveButton.addActionListener(e ->
                controller.execute(nameField.getText(), chosenIndices, matchedLibrary));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        namePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(namePanel);
        add(saveButton);
        add(messageLabel);
    }

    public void setResult(List<Integer> chosenIndices, Library matchedLibrary) {
        this.chosenIndices = chosenIndices;
        this.matchedLibrary = matchedLibrary;
    }

    // presenter -> firePropertyChanged() -> callback -> this
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (viewModel.getError() != null) {
            messageLabel.setText(viewModel.getError());   // Throw warning
        } else {
            messageLabel.setText(viewModel.getMessage()); // Thank you message
        }
    }
}