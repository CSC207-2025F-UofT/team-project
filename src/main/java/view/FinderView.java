package view;

import interface_adapter.search_jobs.SearchJobsController;
import interface_adapter.search_jobs.SearchJobsState;
import interface_adapter.search_jobs.SearchJobsViewModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/** Swing Finder screen: controls, results, accessible status text and application links. */
public final class FinderView extends JPanel {
  private final JTextField query = new JTextField(32);
  private final JButton search = new JButton("Search");
  private final JButton open = new JButton("Open application page");
  private final JTextField link = new JTextField();
  private final JTextArea status = new JTextArea();
  private final JTextArea warning = new JTextArea();
  private final DefaultTableModel model =
      new DefaultTableModel(new String[] {"Title", "Company", "Location"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
          return false;
        }
      };
  private final JTable table = new JTable(model);
  private List<SearchJobsState.Row> rows = List.of();

  /** Connects search controls to adapters; the link opener never changes application records. */
  public FinderView(
      SearchJobsController controller,
      SearchJobsViewModel viewModel,
      LinkOpener linkOpener,
      String sourceLabel) {
    super(new BorderLayout(12, 12));
    setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
    setPreferredSize(new Dimension(960, 600));
    addHeader(controller, sourceLabel);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setAutoCreateRowSorter(true);
    table.setRowHeight(30);
    table.getAccessibleContext().setAccessibleName("Matching internships");
    table.getSelectionModel().addListSelectionListener(event -> updateSelection());
    JScrollPane results = new JScrollPane(table);
    results.setColumnHeaderView(table.getTableHeader());
    add(results, BorderLayout.CENTER);
    addFooter(linkOpener);
    viewModel.addPropertyChangeListener(event -> render(viewModel.getState()));
    render(viewModel.getState());
  }

  private void addHeader(SearchJobsController controller, String sourceLabel) {
    JPanel header = new JPanel(new GridLayout(0, 1, 0, 8));
    JLabel title = new JLabel("Internship Finder");
    title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
    header.add(title);
    header.add(new JLabel(sourceLabel));
    JPanel form = new JPanel(new FlowLayout(FlowLayout.LEADING, 8, 0));
    JLabel label = new JLabel("Keywords");
    label.setLabelFor(query);
    form.add(label);
    form.add(query);
    form.add(search);
    header.add(form);
    header.add(
        new JLabel("Every keyword must match the title or description. Example: software java"));
    search.addActionListener(event -> controller.execute(query.getText()));
    query.addActionListener(event -> controller.execute(query.getText()));
    add(header, BorderLayout.NORTH);
  }

  private void addFooter(LinkOpener opener) {
    JPanel footer = new JPanel(new GridLayout(0, 1, 0, 6));
    configureText(status, "Search status");
    configureText(warning, "Source availability");
    footer.add(status);
    footer.add(warning);
    link.setEditable(false);
    link.getAccessibleContext().setAccessibleName("Selected application URL");
    footer.add(link);
    JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEADING, 0, 0));
    actions.add(open);
    open.addActionListener(
        event -> {
          int selected = table.getSelectedRow();
          if (selected < 0) {
            return;
          }
          try {
            opener.open(rows.get(table.convertRowIndexToModel(selected)).getApplicationUrl());
          } catch (IOException | RuntimeException exception) {
            status.setText("The browser could not be opened. Copy the application URL above.");
            status.setForeground(new Color(160, 30, 30));
          }
        });
    footer.add(actions);
    footer.add(
        new JLabel(
            "Matches indicate relevance, not eligibility. "
                + "Opening a link does not save or submit an application."));
    add(footer, BorderLayout.SOUTH);
  }

  private void configureText(JTextArea area, String accessibleName) {
    area.setEditable(false);
    area.setLineWrap(true);
    area.setWrapStyleWord(true);
    area.setRows(2);
    area.setOpaque(false);
    area.getAccessibleContext().setAccessibleName(accessibleName);
  }

  private void render(SearchJobsState state) {
    rows = state.getRows();
    model.setRowCount(0);
    for (SearchJobsState.Row row : rows) {
      model.addRow(new Object[] {row.getTitle(), row.getCompany(), row.getLocation()});
    }
    query.setEnabled(!state.isLoading());
    search.setEnabled(!state.isLoading());
    status.setText(state.getMessage());
    status.setForeground(state.isError() ? new Color(160, 30, 30) : getForeground());
    warning.setText(state.getWarning());
    warning.setVisible(!state.getWarning().isEmpty());
    updateSelection();
  }

  private void updateSelection() {
    int selected = table.getSelectedRow();
    open.setEnabled(selected >= 0);
    link.setText(
        selected < 0 ? "" : rows.get(table.convertRowIndexToModel(selected)).getApplicationUrl());
  }
}
