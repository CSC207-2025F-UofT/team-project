package view;

import interface_adapters.save_session.SaveSessionController;
import interface_adapters.save_session.SaveSessionViewModel;
import interface_adapters.save_session.SaveSessionViewModelListener;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;


/**
 * The View for when the user is adjusting the timer settings for a study session in the program.
 */
public class SaveSessionView extends JPanel implements SaveSessionViewModelListener {

    private SaveSessionViewModel viewModel;
    private SaveSessionController controller = null;
    private final JFrame settingsFrame = new JFrame();

    public SaveSessionView(SaveSessionViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.addListener(this);

        Font winkle = null;
        try {
            winkle = Font.createFont(Font.TRUETYPE_FONT, new File("src/font/Winkle-Regular.ttf"));
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(winkle);

        Font appleGaramond = null;
        try {
            appleGaramond = Font.createFont(Font.TRUETYPE_FONT, new File("src/font/AppleGaramond.ttf"));
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ge.registerFont(appleGaramond);

        Font appleGaramondBold = null;
        try {
            appleGaramondBold = Font.createFont(Font.TRUETYPE_FONT, new File("src/font/AppleGaramond-Bold.ttf"));
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ge.registerFont(appleGaramondBold);

        Font appleGaramondItalic = null;
        try {
            appleGaramondItalic = Font.createFont(Font.TRUETYPE_FONT, new File("src/font/AppleGaramond-LightItalic.ttf"));
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ge.registerFont(appleGaramondItalic);

        // Main panel containing all the settings components.
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBackground(Color.WHITE);
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 0, 20));

        // Heading section containing the title and subtitle.
        JPanel headingPanel = new JPanel();
        headingPanel.setLayout(new BoxLayout(headingPanel, BoxLayout.Y_AXIS));
        headingPanel.setPreferredSize(new Dimension(425, 80));
        headingPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel headingLabel = new JLabel("Your study rhythm");
        headingLabel.setFont(appleGaramondBold.deriveFont(40f));
        headingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subheadingLabel = new JLabel("Find a pace that feels like you.");
        subheadingLabel.setFont(appleGaramondItalic.deriveFont(20f));
        subheadingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        headingPanel.add(headingLabel);
        headingPanel.add(subheadingLabel);
        headingPanel.setBackground(Color.WHITE);

        // Focus slider and its label.
        JLabel focusSliderLabel = new JLabel("Focus Duration");
        focusSliderLabel.setFont(appleGaramond.deriveFont(22f));
        focusSliderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel focusSliderPanel = new JPanel();
        focusSliderPanel.setLayout(new BoxLayout(focusSliderPanel, BoxLayout.Y_AXIS));
        focusSliderPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSlider focusSlider = new JSlider(0, 60, 25);
        focusSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        focusSlider.setPreferredSize(new Dimension(390, 50));
        focusSlider.setFont(winkle.deriveFont(20f));
        focusSlider.setMajorTickSpacing(5);
        focusSlider.setPaintTicks(true);
        focusSlider.setPaintLabels(true);
        focusSlider.setSnapToTicks(true);

        focusSliderPanel.add(focusSliderLabel);
        focusSliderPanel.add(focusSlider);
        focusSliderPanel.setBackground(Color.WHITE);

        // Break slider and its label.
        JLabel breakSliderLabel = new JLabel("Break Duration");
        breakSliderLabel.setFont(appleGaramond.deriveFont(22f));
        breakSliderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel breakSliderPanel = new JPanel();
        breakSliderPanel.setLayout(new BoxLayout(breakSliderPanel, BoxLayout.Y_AXIS));
        breakSliderPanel.setBackground(Color.WHITE);
        breakSliderPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSlider breakSlider = new JSlider(0, 60, 5);
        breakSlider.setAlignmentX(Component.LEFT_ALIGNMENT);
        breakSlider.setPreferredSize(new Dimension(390, 50));
        breakSlider.setFont(winkle.deriveFont(20f));
        breakSlider.setMajorTickSpacing(5);
        breakSlider.setPaintTicks(true);
        breakSlider.setPaintLabels(true);
        breakSlider.setSnapToTicks(true);

        breakSliderPanel.add(breakSliderLabel);
        breakSliderPanel.add(breakSlider);

        JPanel savePanel = new JPanel();
        savePanel.setLayout(new BoxLayout(savePanel, BoxLayout.Y_AXIS));
        savePanel.setPreferredSize(new Dimension(400, 30));
        savePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        savePanel.setBackground(Color.WHITE);

        JButton saveButton = new JButton("Save");
        saveButton.setPreferredSize(new Dimension(100, 45));
        saveButton.setMaximumSize(new Dimension(100, 45));
        saveButton.setFont(appleGaramond.deriveFont(20f));
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Passes the focus and break durations to the Save Session controller when the user clicks Save.
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.execute(focusSlider.getValue(), breakSlider.getValue());
            }
        });

        savePanel.add(saveButton);

        settingsPanel.add(headingPanel);
        settingsPanel.add(Box.createVerticalStrut(20));
        settingsPanel.add(focusSliderPanel);
        settingsPanel.add(Box.createVerticalStrut(10));
        settingsPanel.add(breakSliderPanel);
        settingsPanel.add(Box.createVerticalStrut(10));
        settingsPanel.add(savePanel);

        settingsFrame.add(settingsPanel);
        settingsFrame.setSize(425, 400);
        settingsFrame.setLocationRelativeTo(null);
    }

    /**
     * Responds to changes in the Save Session View Model.
     */
    @Override
    public void stateChanged() {
        if(viewModel.getState().getErrorMessage() == null) {
            settingsFrame.dispose();
        } else {
            JOptionPane.showMessageDialog(
                    settingsFrame,
                    viewModel.getState().getErrorMessage(),
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Assign the controller to the Save Session View.
     * @param controller Save Session Controller
     */
    public void setController(SaveSessionController controller) {
        this.controller = controller;
    }

    /**
     * Display the timer settings popup window.
     */
    public void showWindow() {
        settingsFrame.setLocationRelativeTo(null);
        settingsFrame.setVisible(true);
    }

}
