package view;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import interface_adapter.track_applications.ApplicationRow;
import interface_adapter.track_applications.TrackApplicationsController;
import interface_adapter.track_applications.TrackApplicationsState;
import interface_adapter.track_applications.TrackApplicationsViewModel;
import interface_adapter.track_applications.TrackApplicationsViewModelListener;

/**
 * The View that lists the adopter's applications and their status.
 */
public final class MyApplicationsView extends JPanel implements TrackApplicationsViewModelListener {

    private static final int PADDING = 12;
    private static final int GAP = 8;
    private static final String BLANK = " ";

    private final TrackApplicationsViewModel trackApplicationsViewModel;
    private TrackApplicationsController trackApplicationsController;

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[] {"Pet", "Submitted", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);
    private final JLabel messageLabel = new JLabel(BLANK);
    private final JLabel noteLabel = new JLabel(BLANK);
    private List<ApplicationRow> rows;

    /**
     * Creates the view.
     * @param trackApplicationsViewModel the view model this view displays
     */
    public MyApplicationsView(TrackApplicationsViewModel trackApplicationsViewModel) {
        this.trackApplicationsViewModel = trackApplicationsViewModel;
        this.trackApplicationsViewModel.addListener(this);
        this.rows = trackApplicationsViewModel.getState().getRows();

        setLayout(new BorderLayout(GAP, GAP));
        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        final JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(event -> refresh());
        final JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("My Applications"), BorderLayout.WEST);
        top.add(refreshButton, BorderLayout.EAST);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(event -> showSelectedNote());

        final JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(messageLabel, BorderLayout.NORTH);
        bottom.add(noteLabel, BorderLayout.SOUTH);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        // Load the latest applications every time the screen is opened.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent event) {
                refresh();
            }
        });
    }

    /**
     * Asks for the current adopter's applications again.
     */
    public void refresh() {
        if (trackApplicationsController == null) {
            return;
        }
        trackApplicationsController.execute(trackApplicationsViewModel.getState().getAdopterId());
    }

    private void showSelectedNote() {
        final int selected = table.getSelectedRow();
        if (selected >= 0 && selected < rows.size()) {
            noteLabel.setText("Shelter's note: " + rows.get(selected).getAdminNote());
        }
        else {
            noteLabel.setText(BLANK);
        }
    }

    @Override
    public void stateChanged() {
        final TrackApplicationsState state = trackApplicationsViewModel.getState();
        rows = state.getRows();
        tableModel.setRowCount(0);
        for (ApplicationRow row : rows) {
            tableModel.addRow(new Object[] {row.getPetName(), row.getSubmittedAt(), row.getStatus()});
        }
        messageLabel.setText(state.getMessage());
        noteLabel.setText(BLANK);
    }

    public String getViewName() {
        return TrackApplicationsViewModel.VIEW_NAME;
    }

    public void setTrackApplicationsController(TrackApplicationsController trackApplicationsController) {
        this.trackApplicationsController = trackApplicationsController;
    }
}
