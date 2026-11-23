package view;

import interface_adapter.rest.RestController;
import interface_adapter.rest.RestViewModel;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.Component;

/**
 * Innis Residence screen: lets the player rest and shows the new day.
 */
public class ResidenceView extends JPanel {
    private final JLabel dayLabel = new JLabel();
    private final JLabel weatherLabel = new JLabel();
    private final JLabel energyLabel = new JLabel();
    private final JLabel saveLabel = new JLabel();
    private final JButton retryButton = new JButton("Retry save");

    public ResidenceView(RestViewModel viewModel, RestController controller, Runnable back) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Innis Residence");
        JButton restButton = new JButton("Rest");
        restButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Resting ends the day, restores energy based on the next day's weather, "
                            + "and saves completed progress. Rest now?",
                    "Rest", JOptionPane.OK_CANCEL_OPTION);
            if (choice == JOptionPane.OK_OPTION) {
                controller.execute();
            }
        });
        retryButton.addActionListener(e -> controller.retrySave());
        retryButton.setVisible(false);

        JButton backButton = new JButton("Back to Street");
        backButton.addActionListener(e -> back.run());

        for (JComponent c : new JComponent[]{title, dayLabel, weatherLabel, energyLabel, restButton,
                saveLabel, retryButton, backButton}) {
            c.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(c);
        }

        viewModel.addPropertyChangeListener(evt -> {
            dayLabel.setText(viewModel.getDayText());
            weatherLabel.setText(viewModel.getWeatherText());
            energyLabel.setText(viewModel.getEnergyText());
            saveLabel.setText(viewModel.getSaveText());
            retryButton.setVisible(viewModel.isRetryAvailable());
        });
    }
}
