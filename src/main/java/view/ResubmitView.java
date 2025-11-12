package view;
import interface_adapter.Resubmit.ResubmitController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResubmitView {
    ResubmitController reSubmitController;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JPanel resubmitPanel = new JPanel();
            resubmitPanel.setLayout(new BoxLayout(resubmitPanel, BoxLayout.Y_AXIS));
            resubmitPanel.add(new JLabel("Are you sure " +
                    "to resubmit your work? Only the last submission will be graded."));
            JButton resubmitButton = new JButton("Resubmit");
            resubmitButton.addActionListener(e -> {
                //TODO: Should go to upload page(SubmitView), select file and then data access. Note
                // here should change viewModel and Controller. To be specific, current time is also needed to
                // compare with ddl
            });
            resubmitPanel.add(resubmitButton);
            JFrame frame = new JFrame("Resubmit");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(resubmitPanel);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
