package view;

import interface_adapter.communicate_request.CommunicateRequestController;
import interface_adapter.communicate_request.CommunicateRequestState;
import interface_adapter.communicate_request.CommunicateRequestViewModel;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;

/**
 * Child-facing communication board.
 */
public class MainBoardPanel extends JPanel {

    private final Navigator navigator;
    private final CommunicateRequestController controller;
    private final CommunicateRequestViewModel viewModel;

    private final JTextField messageBar = new JTextField();
    private final JPanel pictogramGrid =
            new JPanel(new GridLayout(2, 3, 8, 8));

    private final JButton deleteButton = new JButton("Delete");
    private final JButton clearButton = new JButton("Clear");
    private final JButton speakButton = new JButton("Speak");

    public MainBoardPanel(
            Navigator navigator,
            CommunicateRequestController controller,
            CommunicateRequestViewModel viewModel) {

        this.navigator = navigator;
        this.controller = controller;
        this.viewModel = viewModel;

        buildUi();

        viewModel.addPropertyChangeListener(
                event -> render(viewModel.getState()));

        controller.loadBoard();
    }

    private void buildUi() {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildCentrePanel(), BorderLayout.CENTER);
        add(buildCoreMessagePanel(), BorderLayout.SOUTH);
    }

    private JPanel buildTopPanel() {
        ImageIcon icon = Icons.acorn(40);
        JButton caregiverButton = new JButton(icon);

        if (icon == null) {
            caregiverButton.setText("Caregiver");
        }

        caregiverButton.addActionListener(event -> {
            controller.clearDraft();
            navigator.show(ScreenNames.PIN);
        });

        JPanel top = new JPanel(new BorderLayout());
        top.add(caregiverButton, BorderLayout.EAST);

        return top;
    }

    private JPanel buildCentrePanel() {
        messageBar.setEditable(false);
        messageBar.setFont(messageBar.getFont().deriveFont(20f));

        JPanel messageBlock = new JPanel(new BorderLayout(8, 8));
        messageBlock.add(new JLabel("My message"), BorderLayout.NORTH);
        messageBlock.add(messageBar, BorderLayout.CENTER);

        JPanel editButtons = new JPanel(new GridLayout(1, 2, 8, 8));

        deleteButton.addActionListener(
                event -> controller.removeLastSelection());

        clearButton.addActionListener(
                event -> controller.clearDraft());

        editButtons.add(deleteButton);
        editButtons.add(clearButton);

        JPanel messageArea = new JPanel(new BorderLayout(8, 8));
        messageArea.add(messageBlock, BorderLayout.CENTER);
        messageArea.add(editButtons, BorderLayout.SOUTH);

        // UC3 will replace this placeholder listener.
        speakButton.addActionListener(
                event -> System.out.println(
                        "TODO UC3: speak completed message"));

        JPanel centre = new JPanel(new BorderLayout(8, 8));
        centre.add(messageArea, BorderLayout.NORTH);
        centre.add(pictogramGrid, BorderLayout.CENTER);
        centre.add(speakButton, BorderLayout.SOUTH);

        return centre;
    }

    private JPanel buildCoreMessagePanel() {
        JPanel bottom = new JPanel(new GridLayout(1, 3, 8, 8));

        for (String label : new String[]{"Help", "Stop", "Break"}) {
            JButton button = new JButton(label);

            // TODO
            button.addActionListener(
                    event -> System.out.println(
                            "TODO core message: " + label));

            bottom.add(button);
        }

        return bottom;
    }

    private void render(CommunicateRequestState state) {
        messageBar.setText(state.getPreviewText());

        pictogramGrid.removeAll();

        for (CommunicateRequestState.PictogramItem item
                : state.getPictograms()) {

            JButton button = new JButton(item.getLabel());

            button.addActionListener(
                    event -> controller.selectPictogram(item.getId()));

            pictogramGrid.add(button);
        }

        deleteButton.setEnabled(state.hasSelection());
        clearButton.setEnabled(state.hasSelection());
        speakButton.setEnabled(state.hasSelection());

        pictogramGrid.revalidate();
        pictogramGrid.repaint();

        if (state.getErrorMessage() != null) {
            System.err.println(state.getErrorMessage());
        }
    }
}
