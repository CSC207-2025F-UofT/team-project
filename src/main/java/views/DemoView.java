package views;

import interface_adapters.mock_test.MockTestController;

import javax.swing.*;
import java.awt.*;

/**
 * The initial view of the application.
 * It provides a single entry point for the user to start the mock test generation process.
 */
public class DemoView extends JPanel {

    private MockTestController mockTestController = null;

    public DemoView() {
        initializeUI();
    }

    public void setMockTestController(MockTestController mockTestController) {
        this.mockTestController = mockTestController;
    }

    private void initializeUI() {
        this.setLayout(new GridBagLayout()); // Center the button
        this.setPreferredSize(new Dimension(800, 600));

        JButton generateTestButton = new JButton("Generate New Mock Test");
        generateTestButton.setFont(new Font("SansSerif", Font.BOLD, 18));
        generateTestButton.setPreferredSize(new Dimension(300, 80));

        // When the button is clicked, call the controller to execute the use case.
        generateTestButton.addActionListener(e -> {
            try {
                // In a real application, you'd get the course ID from a dropdown or a list.
                // For this demo, we'll hardcode the ID of the dummy course.
                String dummyCourseId = "CS207"; // This must match the ID in Main.java
                mockTestController.execute(dummyCourseId);
            } catch (Exception ex) {
                // In a real app, this would trigger the presenter's fail view.
                JOptionPane.showMessageDialog(this, "Error generating test: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        this.add(generateTestButton);
    }
}