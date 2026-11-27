package view;

import interface_adapter.create_project.CreateProjectController;
import interface_adapter.create_project.CreateProjectViewModel;

import javax.swing.*;
import java.awt.*;

public class CreateProjectView extends JPanel {
    private final JTextField nameField =
            new JTextField(15);
    private final JTextField widthField =
            new JTextField("800", 6);
    private final JTextField heightField =
            new JTextField("600", 6);

    private final JLabel resultLabel = new JLabel(" ");
    private final JPanel preview = new JPanel();

    public CreateProjectView(
            CreateProjectController controller,
            CreateProjectViewModel viewModel) {

        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(4, 2, 5, 5));

        form.add(new JLabel("Project name:"));
        form.add(nameField);

        form.add(new JLabel("Width:"));
        form.add(widthField);

        form.add(new JLabel("Height:"));
        form.add(heightField);

        JButton createButton = new JButton("Create");
        form.add(new JLabel());
        form.add(createButton);

        preview.setBackground(Color.WHITE);
        preview.setBorder(
                BorderFactory.createLineBorder(Color.GRAY)
        );

        add(form, BorderLayout.NORTH);
        add(preview, BorderLayout.CENTER);
        add(resultLabel, BorderLayout.SOUTH);

        createButton.addActionListener(event -> {
            try {
                int width = Integer.parseInt(
                        widthField.getText().trim()
                );
                int height = Integer.parseInt(
                        heightField.getText().trim()
                );

                controller.execute(
                        nameField.getText(), width, height
                );
            } catch (NumberFormatException exception) {
                resultLabel.setText(
                        "Width and height must be whole numbers."
                );
            }
        });

        viewModel.addPropertyChangeListener(event -> {
            resultLabel.setText(viewModel.getMessage());

            if (viewModel.isSuccessful()) {
                preview.removeAll();
                preview.add(new JLabel(
                        viewModel.getProjectName()
                                + " — "
                                + viewModel.getWidth()
                                + " × "
                                + viewModel.getHeight()
                ));
                preview.revalidate();
                preview.repaint();
            }
        });
    }
}