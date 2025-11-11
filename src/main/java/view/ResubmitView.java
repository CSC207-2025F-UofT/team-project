package view;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResubmitView {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JPanel resubmitPanel = new JPanel();
            resubmitPanel.add(new JLabel("Are you sure " +
                    "to resubmit your work? Only the last submission will be graded."));
            JButton resubmitButton = new JButton("Reubmit");
            resubmitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //TODO: Should go to upload page(SubmitView), select file and then data access. Note
                    // here should change viewModel and Controller. To be specific, let
                }
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
