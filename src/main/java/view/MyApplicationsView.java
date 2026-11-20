package view;

import java.awt.BorderLayout;
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
public class MyApplicationsView extends JPanel implements TrackApplicationsViewModelListener {

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
    private final JLabel messageLabel = new JLabel(" ");
    private final JLabel noteLabel = new JLabel(" ");
    private List<ApplicationRow> rows;

    /**
     * Creates the view.
     * @param trackApplicationsViewModel the view model this view displays
     */
    public MyApplicationsView(TrackApplicationsViewModel trackApplicationsViewModel) {
        this.trackApplicationsViewModel = trackApplicationsViewModel;
        this.trackApplicationsViewModel.addListener(this);
        this.rows = trackApplicationsViewModel.getState().getRows();

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

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
    }

    /**
     * Asks for the current adopter's applications again.
     */
    public void refresh() {
        trackApplicationsController.execute(trackApplicationsViewModel.getState().getAdopterId());
    }

    private void showSelectedNote() {
        final int selected = table.getSelectedRow();
        if (selected >= 0 && selected < rows.size()) {
            noteLabel.setText("Shelter's note: " + rows.get(selected).getAdminNote());
        } else {
            noteLabel.setText(" ");
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
        noteLabel.setText(" ");
    }

    public String getViewName() {
        return TrackApplicationsViewModel.VIEW_NAME;
    }

    public void setTrackApplicationsController(TrackApplicationsController trackApplicationsController) {
        this.trackApplicationsController = trackApplicationsController;
    }
}
