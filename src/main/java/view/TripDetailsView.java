package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import interface_adapter.ViewManagerModel;
import interface_adapter.ViewNames;
import interface_adapter.manipulate_trips.ManipulateTripsController;
import interface_adapter.manipulate_trips.MergeOption;
import interface_adapter.manipulate_trips.PhotoTile;
import interface_adapter.manipulate_trips.TripDetailsState;
import interface_adapter.manipulate_trips.TripDetailsViewModel;
import view.components.PhotoTileRenderer;
import view.components.ThumbnailCache;

/**
 * One trip: header, Split / Merge actions, and its photos in chronological order.
 * Not a nav tab.
 */
public class TripDetailsView extends JPanel implements PropertyChangeListener {
    static final String EMPTY_MESSAGE = "This trip has no photos.";
    static final String SELECT_PHOTO_FIRST = "Select the photo where the new trip should start.";
    static final String NO_OTHER_TRIPS = "There are no other trips to merge with.";
    private static final Color ERROR_COLOR = new Color(0xB3261E);
    private static final Color NOTICE_COLOR = new Color(0x1E7B34);

    private final TripDetailsViewModel viewModel;
    private final ViewManagerModel viewManagerModel;
    private final DefaultListModel<PhotoTile> photoModel = new DefaultListModel<>();
    private final JList<PhotoTile> photoList = new JList<>(photoModel);
    private final JLabel nameLabel = new JLabel();
    private final JLabel subtitleLabel = new JLabel();
    private final JLabel messageLabel = new JLabel();
    private final JButton backButton = new JButton("← All trips");
    private final JButton splitButton = new JButton("Split here…");
    private final JButton mergeButton = new JButton("Merge with…");
    private ManipulateTripsController controller;

    public TripDetailsView(TripDetailsViewModel viewModel, ViewManagerModel viewManagerModel,
                           ThumbnailCache thumbnails) {
        super(new BorderLayout());
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
        viewModel.addPropertyChangeListener(this);

        // Photo grid
        photoList.setCellRenderer(new PhotoTileRenderer(thumbnails, photoList::repaint));
        photoList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        photoList.setVisibleRowCount(-1);
        photoList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        photoList.setFixedCellWidth(thumbnails.getSize() + 24);
        photoList.setFixedCellHeight(thumbnails.getSize() + 40);

        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD, 20f));
        messageLabel.setVisible(false);

        backButton.addActionListener(e -> viewManagerModel.setActiveView(ViewNames.TRIPS));
        splitButton.addActionListener(e -> onSplitClicked());
        mergeButton.addActionListener(e -> onMergeClicked());

        add(buildHeader(), BorderLayout.NORTH);
        add(new JScrollPane(photoList), BorderLayout.CENTER);
    }

    private JComponent buildHeader() {
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.add(splitButton);
        actions.add(mergeButton);

        JPanel topRow = new JPanel(new BorderLayout());
        topRow.add(backButton, BorderLayout.WEST);
        topRow.add(actions, BorderLayout.EAST);

        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(12, 12, 8, 12));
        for (JComponent part : new JComponent[] {topRow, nameLabel, subtitleLabel, messageLabel}) {
            part.setAlignmentX(LEFT_ALIGNMENT);   // BoxLayout needs every child aligned the same way
            header.add(part);
            header.add(Box.createVerticalStrut(6));
        }
        return header;
    }

    public void setManipulateTripsController(ManipulateTripsController controller) {
        this.controller = controller;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SwingThread.runOnEdt(() -> render(viewModel.getState()));
    }

    private void render(TripDetailsState state) {
        photoModel.clear();
        photoModel.addAll(state.getPhotos());

        boolean hasTrip = state.getTripId() != null;
        nameLabel.setText(state.getName());
        subtitleLabel.setText(hasTrip ? state.getDateRange() + " · " + state.getPhotoCount() : "");
        splitButton.setEnabled(hasTrip);
        mergeButton.setEnabled(hasTrip);

        if (state.getErrorMessage() != null) {
            showMessage(state.getErrorMessage(), ERROR_COLOR);
        } else if (state.getNoticeMessage() != null) {
            showMessage(state.getNoticeMessage(), NOTICE_COLOR);
        } else if (hasTrip && state.getPhotos().isEmpty()) {
            showMessage(EMPTY_MESSAGE, getForeground());
        } else {
            messageLabel.setVisible(false);
        }
    }

    private void showMessage(String text, Color color) {
        messageLabel.setText(text);
        messageLabel.setForeground(color);
        messageLabel.setVisible(true);
    }

    /** Asks for the new trip's name, then splits at the selected photo. */
    private void onSplitClicked() {
        PhotoTile selected = photoList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, SELECT_PHOTO_FIRST, "Split trip", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Object name = JOptionPane.showInputDialog(this,
                "The selected photo and every photo after it will move to a new trip.\nName for the new trip:",
                "Split trip", JOptionPane.PLAIN_MESSAGE, null, null, viewModel.getState().getSuggestedSplitName());
        if (name != null) {   // null means the user cancelled
            requestSplit(selected.getPhotoId(), name.toString());
        }
    }

    /** Lets the user pick another trip, then merges the two. */
    private void onMergeClicked() {
        List<MergeOption> options = viewModel.getState().getMergeOptions();
        if (options.isEmpty()) {
            JOptionPane.showMessageDialog(this, NO_OTHER_TRIPS, "Merge trips", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Object choice = JOptionPane.showInputDialog(this,
                "Merge “" + viewModel.getState().getName() + "” with:\n"
                        + "(The merged trip keeps the name of the earlier trip.)",
                "Merge trips", JOptionPane.PLAIN_MESSAGE, null, options.toArray(), options.get(0));
        if (choice != null) {
            requestMerge(((MergeOption) choice).getTripId());
        }
    }

    // Split out from the click handlers so tests don't need to open dialogs.
    void requestSplit(String firstPhotoId, String newTripName) {
        if (controller != null) {
            controller.splitTrip(viewModel.getState().getTripId(), firstPhotoId, newTripName);
        }
    }

    void requestMerge(String otherTripId) {
        if (controller != null) {
            controller.mergeTrips(List.of(viewModel.getState().getTripId(), otherTripId));
        }
    }

    // Package-private accessors for tests.
    DefaultListModel<PhotoTile> getPhotoModel() { return photoModel; }
    JLabel getNameLabel() { return nameLabel; }
    JLabel getSubtitleLabel() { return subtitleLabel; }
    JLabel getMessageLabel() { return messageLabel; }
    JButton getBackButton() { return backButton; }
}