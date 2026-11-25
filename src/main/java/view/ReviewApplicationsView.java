package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import interface_adapter.review_applications.ReviewApplicationsController;
import interface_adapter.review_applications.ReviewApplicationsState;
import interface_adapter.review_applications.ReviewApplicationsViewModel;
import interface_adapter.review_applications.ReviewApplicationsViewModelListener;

/**
 * The View where the admin lists applications, filters them by pet and status, and reads one application's answers.
 */
public final class ReviewApplicationsView extends JPanel implements ReviewApplicationsViewModelListener {

    private static final int PADDING = 12;
    private static final int GAP = 8;
    private static final int PET_ID_COLUMNS = 10;
    private static final int DETAILS_ROWS = 14;
    private static final int DETAILS_COLUMNS = 30;
    private static final double LIST_SHARE = 0.55;
    private static final String BLANK = " ";

    private final ReviewApplicationsViewModel reviewApplicationsViewModel;
    private ReviewApplicationsController reviewApplicationsController;

    private final JTextField petIdField = new JTextField(PET_ID_COLUMNS);
    private final JComboBox<String> statusBox =
            new JComboBox<>(ReviewApplicationsViewModel.STATUS_OPTIONS.toArray(new String[0]));

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[] {"Pet", "Adopter", "Submitted", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);
    private final JLabel messageLabel = new JLabel(BLANK);

    private final JTextArea detailsArea = new JTextArea(DETAILS_ROWS, DETAILS_COLUMNS);
    private final JLabel detailMessageLabel = new JLabel(BLANK);

    /** The ids of the applications in the table, in table order. */
    private List<String> shownIds = new ArrayList<>();
    /** True while the table is being refilled, so selection changes made by the view itself are ignored. */
    private boolean updatingTable;

    /**
     * Creates the view.
     * @param reviewApplicationsViewModel the view model this view displays
     */
    public ReviewApplicationsView(ReviewApplicationsViewModel reviewApplicationsViewModel) {
        this.reviewApplicationsViewModel = reviewApplicationsViewModel;
        this.reviewApplicationsViewModel.addListener(this);

        setLayout(new BorderLayout(GAP, GAP));
        setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        add(buildTop(), BorderLayout.NORTH);
        final JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildList(), buildDetails());
        split.setResizeWeight(LIST_SHARE);
        add(split, BorderLayout.CENTER);

        // Load the latest applications every time the screen is opened.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent event) {
                refresh();
            }
        });
    }

    private JPanel buildTop() {
        final JButton filterButton = new JButton("Filter");
        filterButton.addActionListener(event -> refresh());
        petIdField.addActionListener(event -> refresh());

        final JButton showAllButton = new JButton("Show all");
        showAllButton.addActionListener(event -> {
            petIdField.setText("");
            statusBox.setSelectedItem(ReviewApplicationsViewModel.ALL_STATUSES);
            refresh();
        });

        final JPanel filters = new JPanel(new FlowLayout(FlowLayout.RIGHT, GAP, 0));
        filters.add(new JLabel("Pet ID:"));
        filters.add(petIdField);
        filters.add(new JLabel("Status:"));
        filters.add(statusBox);
        filters.add(filterButton);
        filters.add(showAllButton);

        final JPanel top = new JPanel(new BorderLayout());
        top.add(new JLabel("Review Applications"), BorderLayout.WEST);
        top.add(filters, BorderLayout.EAST);
        return top;
    }

    private JPanel buildList() {
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && !updatingTable) {
                openSelectedApplication();
            }
        });

        final JPanel list = new JPanel(new BorderLayout(0, GAP));
        list.add(new JScrollPane(table), BorderLayout.CENTER);
        list.add(messageLabel, BorderLayout.SOUTH);
        return list;
    }

    private JPanel buildDetails() {
        detailsArea.setEditable(false);
        detailsArea.setLineWrap(true);
        detailsArea.setWrapStyleWord(true);

        final JPanel details = new JPanel(new BorderLayout(0, GAP));
        details.add(new JLabel("Adopter's answers"), BorderLayout.NORTH);
        details.add(new JScrollPane(detailsArea), BorderLayout.CENTER);
        details.add(detailMessageLabel, BorderLayout.SOUTH);
        return details;
    }

    /**
     * Asks for the applications again, using the filters currently entered.
     */
    public void refresh() {
        if (reviewApplicationsController == null) {
            return;
        }
        reviewApplicationsController.execute(petIdField.getText().trim(), (String) statusBox.getSelectedItem());
    }

    private void openSelectedApplication() {
        final int selected = table.getSelectedRow();
        if (reviewApplicationsController != null && selected >= 0 && selected < shownIds.size()) {
            reviewApplicationsController.viewApplication(shownIds.get(selected));
        }
    }

    @Override
    public void stateChanged() {
        final ReviewApplicationsState state = reviewApplicationsViewModel.getState();

        // Only refill the table when the list itself changed, so opening an application keeps its row selected.
        if (!state.getApplicationIds().equals(shownIds)) {
            updatingTable = true;
            tableModel.setRowCount(0);
            for (int i = 0; i < state.getApplicationCount(); i++) {
                tableModel.addRow(new Object[] {state.getPetNames().get(i), state.getAdopterNames().get(i),
                    state.getSubmittedDates().get(i), state.getStatuses().get(i)});
            }
            shownIds = new ArrayList<>(state.getApplicationIds());
            updatingTable = false;
        }
        messageLabel.setText(textOrBlank(state.getMessage()));

        if (state.hasDetails()) {
            detailsArea.setText(detailsText(state));
            detailsArea.setCaretPosition(0);
        }
        else {
            detailsArea.setText("");
        }
        detailMessageLabel.setText(textOrBlank(state.getDetailMessage()));
    }

    private static String detailsText(ReviewApplicationsState state) {
        return "Pet: " + state.getDetailPetName() + "\n"
                + "Adopter: " + state.getDetailAdopterName() + "\n"
                + "Submitted: " + state.getDetailSubmittedAt() + "\n"
                + "Status: " + state.getDetailStatus() + "\n"
                + "\n"
                + "Housing type: " + state.getDetailHousingType() + "\n"
                + "Experience with pets: " + state.getDetailPetExperience() + "\n"
                + "Household members: " + state.getDetailHouseholdMembers() + "\n"
                + "Contact: " + state.getDetailContactInfo() + "\n"
                + "Adopter's note: " + state.getDetailNote() + "\n"
                + "\n"
                + "Shelter's note: " + state.getDetailAdminNote();
    }

    private static String textOrBlank(String text) {
        if (text == null || text.isEmpty()) {
            return BLANK;
        }
        return text;
    }

    public String getViewName() {
        return ReviewApplicationsViewModel.VIEW_NAME;
    }

    public void setReviewApplicationsController(ReviewApplicationsController reviewApplicationsController) {
        this.reviewApplicationsController = reviewApplicationsController;
    }
}
