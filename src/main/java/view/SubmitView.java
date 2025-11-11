package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SubmitView {
    public static void main(String[] args) {
        JPanel submitPanel = new JPanel();
        JTextField submitText = new JTextField();
        JFrame frame = new JFrame("Upload Example");
        JButton uploadButton = new JButton("Choose File");

        uploadButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select a file to upload");

            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                JOptionPane.showMessageDialog(frame,
                        "File selected: " + selectedFile.getAbsolutePath());
                // TODO: send selectedFile to Controller
            } else {
                JOptionPane.showMessageDialog(frame, "No file selected");
            }
        });

        frame.add(uploadButton);
    }
}
