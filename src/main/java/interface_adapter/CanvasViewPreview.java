package interface_adapter;

import view.CanvasView;

import javax.swing.*;
import java.awt.*;

/**
 * Temporary class to see how the canvas view looks.
 */
public class CanvasViewPreview {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // Create a dummy ViewModel
            CanvasViewModel viewModel = new CanvasViewModel();
            viewModel.setTitle("My Drawing");
            viewModel.setCanvasWidth(800);
            viewModel.setCanvasHeight(600);
            viewModel.setCanvasColour(Color.WHITE);

            // Create the view
            CanvasView canvasView = new CanvasView(viewModel);

            // Display it
            JFrame frame = new JFrame("Canvas Preview");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 800);
            frame.setLocationRelativeTo(null);
            frame.add(canvasView);
            frame.setVisible(true);
        });
    }
}
