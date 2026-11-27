package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.List;
import java.util.stream.Collectors;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import interface_adapter.manipulate_trips.ManipulateTripsController;
import interface_adapter.manipulate_trips.TripListItem;
import interface_adapter.manipulate_trips.TripListState;
import interface_adapter.manipulate_trips.TripListViewModel;
import view.components.ThumbnailCache;
import view.components.TripCardRenderer;

/** The Trips tab: scrollable list of trip cards. */
public class TripListView extends JPanel implements ActivatableView, PropertyChangeListener {
    static final String EMPTY_MESSAGE = "No trips yet - import photos to get started.";
    static final String HINT = "Double-click a trip to open it. Ctrl/Cmd-click to select several.";
    static final String SELECT_TWO_TO_MERGE = "Select two or more trips to merge (Ctrl/Cmd-click).";

    private final TripListViewModel viewModel;
    private final DefaultListModel<TripListItem> listModel = new DefaultListModel<>();
    private final JList<TripListItem> tripList = new JList<>(listModel);
    private final JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
    private final JButton mergeButton = new JButton("Merge selected");
    private ManipulateTripsController controller;

    public TripListView(TripListViewModel viewModel, ThumbnailCache thumbnails) {
        super(new BorderLayout());
        this.viewModel = viewModel;
        viewModel.addPropertyChangeListener(this);

        tripList.setCellRenderer(new TripCardRenderer(thumbnails, tripList::repaint));
        tripList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        messageLabel.setBorder(BorderFactory.createEmptyBorder(24, 0, 24, 0));
        messageLabel.setVisible(false);


        // Single click selects, so opening needs double-click or Enter.
        tripList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = tripList.locationToIndex(e.getPoint());
                if (e.getClickCount() == 2 && index >= 0 && tripList.getCellBounds(index, index).contains(e.getPoint())) {
                    openTrip(listModel.get(index).getTripId());
                }
            }
        });
        tripList.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "openTrip");
        tripList.getActionMap().put("openTrip", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TripListItem selected = tripList.getSelectedValue();
                if (selected != null) {
                    openTrip(selected.getTripId());
                }
            }
        });
        mergeButton.addActionListener(e -> onMergeClicked());

        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBorder(BorderFactory.createEmptyBorder(8, 12, 0, 12));
        toolbar.add(new JLabel(HINT), BorderLayout.WEST);
        toolbar.add(mergeButton, BorderLayout.EAST);

        JPanel north = new JPanel(new BorderLayout());
        north.add(toolbar, BorderLayout.NORTH);
        north.add(messageLabel, BorderLayout.CENTER);

        add(north, BorderLayout.NORTH);
        add(new JScrollPane(tripList), BorderLayout.CENTER);
    }

    public void setManipulateTripsController(ManipulateTripsController controller) {
        this.controller = controller;
    }

    @Override 
    public void onActivated() {
        if (controller != null) {
            controller.listTrips();
        }
    }
    
    void openTrip(String tripId) {
        if (controller != null) {
            controller.viewTrip(tripId);
        }
    }

    private void onMergeClicked() {
        List<TripListItem> selected = tripList.getSelectedValuesList();
        if (selected.size() < 2) {
            JOptionPane.showMessageDialog(this, SELECT_TWO_TO_MERGE, "Merge trips", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
                "Merge " + selected.size() + " trips into one?\nThe merged trip keeps the name of the earliest trip.",
                "Merge Trips", JOptionPane.OK_CANCEL_OPTION);
        if (answer == JOptionPane.OK_OPTION) {
            requestMerge(selected.stream().map(TripListItem::getTripId).collect(Collectors.toList()));
        }
    }

    void requestMerge(List<String> tripIds) {
        if (controller != null) {
            controller.mergeTrips(tripIds);
        }
    }

    @Override 
    public void propertyChange(PropertyChangeEvent evt) {
        SwingThread.runOnEdt(() -> render(viewModel.getState()));
    }

    private void render(TripListState state) {
        listModel.clear();
        listModel.addAll(state.getItems());

        if (state.getErrorMessage() != null) {
            showMessage(state.getErrorMessage());
        } else if (state.getItems().isEmpty()) {
            showMessage(EMPTY_MESSAGE);
        } else {
            messageLabel.setVisible(false);
        }
    }

    private void showMessage(String text) {
        messageLabel.setText(text);
        messageLabel.setVisible(true);
    }

    // Package-private accessors for tests.
    DefaultListModel<TripListItem> getListModel() { return listModel; }
    JLabel getMessageLabel() { return messageLabel; }
}