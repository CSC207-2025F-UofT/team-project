package view;

import interface_adapter.recommendation.RecommendationController;
import interface_adapter.recommendation.RecommendationRow;
import interface_adapter.recommendation.RecommendationViewModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.IOException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/** Ranked internships with their matched skills and a copyable link for the selected row. */
public final class RecommendationView extends JPanel {
  private final DefaultTableModel rows =
      new DefaultTableModel(
          new String[] {"Position", "Company", "Location", "Work mode", "Matched skills"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
          return false;
        }
      };
  private final JTable table = new JTable(rows);
  private final JLabel status = new JLabel();
  private final JLabel warning = new JLabel();
  private final JTextField url = new JTextField();
  private final JButton open = new JButton("Open application page");
  private final JButton refresh = new JButton("Refresh recommendations");
  private List<RecommendationRow> displayed = List.of();

  /** Binds the ranked table and its navigation to the supplied adapters. */
  public RecommendationView(
      RecommendationController controller,
      RecommendationViewModel model,
      LinkOpener links,
      String sourceLabel,
      Runnable editProfile) {
    super(new BorderLayout(8, 8));
    setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
    JPanel header = new JPanel();
    header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
    header.add(new JLabel(sourceLabel));
    header.add(status);
    header.add(warning);
    add(header, BorderLayout.NORTH);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setFillsViewportHeight(true);
    table.setRowHeight(30);
    table.getAccessibleContext().setAccessibleName("Recommended internships and matched skills");
    int[] widths = {330, 100, 230, 95, 175};
    for (int column = 0; column < widths.length; column++) {
      table.getColumnModel().getColumn(column).setPreferredWidth(widths[column]);
    }
    table.setDefaultRenderer(
        Object.class,
        new DefaultTableCellRenderer() {
          @Override
          protected void setValue(Object value) {
            super.setValue(value);
            setToolTipText(getText());
          }
        });
    JScrollPane results = new JScrollPane(table);
    results.setColumnHeaderView(table.getTableHeader());
    add(results, BorderLayout.CENTER);
    url.setEditable(false);
    JLabel urlLabel = new JLabel("Application URL");
    urlLabel.setLabelFor(url);
    JPanel address = new JPanel(new BorderLayout(6, 0));
    address.add(urlLabel, BorderLayout.WEST);
    address.add(url, BorderLayout.CENTER);
    JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEADING, 4, 0));
    JButton edit = new JButton("Edit profile");
    actions.add(refresh);
    actions.add(edit);
    actions.add(open);
    JPanel footer = new JPanel(new BorderLayout(0, 6));
    footer.add(address, BorderLayout.NORTH);
    footer.add(actions, BorderLayout.SOUTH);
    add(footer, BorderLayout.SOUTH);

    table.getSelectionModel().addListSelectionListener(event -> selectRow());
    open.addActionListener(
        event -> {
          try {
            links.open(url.getText());
          } catch (IOException | SecurityException exception) {
            status.setText("Could not open the browser. You can copy the application URL.");
          }
        });
    refresh.addActionListener(event -> controller.execute());
    edit.addActionListener(event -> editProfile.run());
    model.addPropertyChangeListener(event -> render(model.getState()));
    render(model.getState());
  }

  private void render(RecommendationViewModel.State state) {
    table.clearSelection();
    rows.setRowCount(0);
    displayed = state.getRows();
    for (RecommendationRow row : displayed) {
      rows.addRow(
          new Object[] {
            row.getTitle(),
            row.getCompany(),
            row.getLocation(),
            row.getArrangement(),
            row.getSkills()
          });
    }
    selectRow();
    status.setText(state.getMessage());
    warning.setText(state.getWarning());
    warning.setVisible(!state.getWarning().isEmpty());
    refresh.setEnabled(!state.isLoading());
  }

  private void selectRow() {
    int selected = table.getSelectedRow();
    url.setText(selected < 0 ? "" : displayed.get(table.convertRowIndexToModel(selected)).getUrl());
    open.setEnabled(selected >= 0);
  }
}
