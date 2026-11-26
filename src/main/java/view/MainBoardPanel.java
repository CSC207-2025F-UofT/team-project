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
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

/** Child-facing communication board. */
public class MainBoardPanel extends JPanel {
    private final Navigator navigator;
    private final CommunicateRequestController controller;
    private final CommunicateRequestViewModel viewModel;
    private final JTextField messageBar = new JTextField();
    private final JLabel status = new JLabel(" ");
    private final JPanel pictogramGrid = new JPanel(new GridLayout(2, 3, 8, 8));
    private final JButton deleteButton = new JButton("Delete");
    private final JButton clearButton = new JButton("Clear");
    private final JButton speakButton = new JButton("Speak");
    private final PictogramIconLoader icons = new PictogramIconLoader();
    private List<CommunicateRequestState.PictogramItem> displayedItems;

    public MainBoardPanel(Navigator navigator, CommunicateRequestController controller,
                          CommunicateRequestViewModel viewModel) {
        this.navigator = navigator;
        this.controller = controller;
        this.viewModel = viewModel;
        buildUi();
        viewModel.addPropertyChangeListener(event -> render(viewModel.getState()));
        // Refresh after caregiver edits, even if the DAO replaces the board object.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent event) {
                controller.loadBoard();
            }
        });
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
        caregiverButton.setName("caregiver");
        if (icon == null) {
            caregiverButton.setText("Caregiver");
        }
        caregiverButton.getAccessibleContext().setAccessibleName("Caregiver mode");
        caregiverButton.addActionListener(event -> {
            controller.clearDraft();
            navigator.show(ScreenNames.PIN);
        });
        JPanel top = new JPanel(new BorderLayout());
        top.add(caregiverButton, BorderLayout.EAST);
        return top;
    }

    private JPanel buildCentrePanel() {
        messageBar.setName("messagePreview");
        messageBar.setEditable(false);
        messageBar.setFont(messageBar.getFont().deriveFont(20f));
        status.setName("requestStatus");
        JPanel messageBlock = new JPanel(new BorderLayout(8, 8));
        messageBlock.add(new JLabel("My message"), BorderLayout.NORTH);
        messageBlock.add(messageBar, BorderLayout.CENTER);
        messageBlock.add(status, BorderLayout.SOUTH);
        JPanel editButtons = new JPanel(new GridLayout(1, 2, 8, 8));
        deleteButton.addActionListener(event -> controller.removeLastSelection());
        clearButton.addActionListener(event -> controller.clearDraft());
        editButtons.add(deleteButton);
        editButtons.add(clearButton);
        JPanel messageArea = new JPanel(new BorderLayout(8, 8));
        messageArea.add(messageBlock, BorderLayout.CENTER);
        messageArea.add(editButtons, BorderLayout.SOUTH);
        // UC3 owns speech and recording; this listener remains its integration point.
        speakButton.addActionListener(event -> System.out.println("TODO UC3: speak completed message"));
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
            // Preserve the existing core-message integration point.
            button.addActionListener(event -> System.out.println("TODO core message: " + label));
            bottom.add(button);
        }
        return bottom;
    }

    private void render(CommunicateRequestState state) {
        messageBar.setText(state.getPreviewText());
        String error = state.getErrorMessage();
        status.setText(error != null ? error : state.getPictograms().isEmpty()
                ? "No pictograms available." : " ");
        if (!sameItems(displayedItems, state.getPictograms())) {
            displayedItems = new ArrayList<>(state.getPictograms());
            renderPictograms(displayedItems);
        }
        deleteButton.setEnabled(state.hasSelection());
        clearButton.setEnabled(state.hasSelection());
        speakButton.setEnabled(state.hasSelection());
    }

    private void renderPictograms(List<CommunicateRequestState.PictogramItem> items) {
        pictogramGrid.removeAll();
        for (CommunicateRequestState.PictogramItem item : items) {
            JButton button = new JButton(item.getLabel());
            button.setName("pictogram-" + item.getId());
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.addActionListener(event -> controller.selectPictogram(item.getId()));
            icons.load(item.getImageLocation(), button);
            pictogramGrid.add(button);
        }
        pictogramGrid.revalidate();
        pictogramGrid.repaint();
    }

    private static boolean sameItems(List<CommunicateRequestState.PictogramItem> previous,
                                     List<CommunicateRequestState.PictogramItem> next) {
        if (previous == null || previous.size() != next.size()) {
            return false;
        }
        for (int i = 0; i < next.size(); i++) {
            CommunicateRequestState.PictogramItem first = previous.get(i);
            CommunicateRequestState.PictogramItem second = next.get(i);
            if (first.getId() != second.getId() || !first.getLabel().equals(second.getLabel())
                    || !first.getImageLocation().equals(second.getImageLocation())) {
                return false;
            }
        }
        return true;
    }
}
