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
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/** Swing Finder screen: controls, results, accessible status text and application links. */
public final class FinderView extends JPanel {
  /** Listing sources supported by the search form. */
  public enum Source {
    BOARDS("", "", "Every keyword must match the title or description. Example: software java"),
    JOOBLE(
        "Jooble",
        "https://ca.jooble.org/",
        "Search across Canada or enter a location. Load up to 10 matching internships at a time.");

    private final String label;
    private final String homepage;
    private final String hint;

    Source(String label, String homepage, String hint) {
      this.label = label;
      this.homepage = homepage;
      this.hint = hint;
    }

    public String getLabel() {
      return label;
    }
  }

  private final JTextField company = new JTextField(13);
  private final JButton more = new JButton("Load more");
  private final JTextField query = new JTextField(22);
  private final JTextField location = new JTextField(13);
  private final Source source;
  private final CompanySelector companies;
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
    this(controller, viewModel, linkOpener, sourceLabel, Source.BOARDS, Map.of());
  }

  /** Builds controls and attribution for the selected listing source. */
  public FinderView(
      SearchJobsController controller,
      SearchJobsViewModel viewModel,
      LinkOpener linkOpener,
      String sourceLabel,
      Source source,
      Map<String, String> boardNames) {
    super(new BorderLayout(12, 12));
    companies =
        boardNames.isEmpty()
            ? null
            : new CompanySelector(
                boardNames,
                () ->
                    viewModel.setState(
                        new SearchJobsState(
                            List.of(),
                            "Press Search to see matching internships.",
                            "",
                            false,
                            false)),
                search::doClick);
    this.source = source;
    setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
    setPreferredSize(new Dimension(960, 600));
    addHeader(controller, sourceLabel);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setAutoCreateRowSorter(true);
    table.setRowHeight(30);
    table.getAccessibleContext().setAccessibleName("Matching internships");
    table.getSelectionModel().addListSelectionListener(event -> updateSelection());
    if (source != Source.BOARDS) {
      addAttribution(linkOpener);
    }
    JScrollPane results = new JScrollPane(table);
    results.setColumnHeaderView(table.getTableHeader());
    add(results, BorderLayout.CENTER);
    addFooter(linkOpener);
    if (source == Source.JOOBLE) {
      more.addActionListener(event -> controller.loadMore());
      resetOnCriteriaChange(viewModel);
    }
    viewModel.addPropertyChangeListener(event -> render(viewModel.getState()));
    render(viewModel.getState());
  }

  private void addHeader(SearchJobsController controller, String sourceLabel) {
    JPanel header = new JPanel(new GridLayout(0, 1, 0, 8));
    JLabel title = new JLabel("Internship Finder");
    title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
    header.add(title);
    header.add(new JLabel(sourceLabel));
    if (companies != null) {
      header.add(companies);
    }
    JPanel form = new JPanel(new FlowLayout(FlowLayout.LEADING, 8, 0));
    if (source == Source.JOOBLE) {
      form.add(labeledField("Keywords (required)", query));
      form.add(labeledField("Company (optional)", company));
      form.add(labeledField("Location (optional)", location));
    } else {
      JLabel label = new JLabel("Keywords");
      label.setLabelFor(query);
      form.add(label);
      form.add(query);
    }
    form.add(search);
    header.add(form);
    header.add(new JLabel(source.hint));
    search.addActionListener(
        event -> {
          if (source == Source.JOOBLE) {
            controller.execute(query.getText(), company.getText(), location.getText());
          } else if (companies != null) {
            controller.executeForBoard(query.getText(), companies.selectedBoard());
          } else {
            controller.execute(query.getText());
          }
        });
    query.addActionListener(event -> search.doClick());
    company.addActionListener(event -> search.doClick());
    location.addActionListener(event -> search.doClick());
    add(header, BorderLayout.NORTH);
  }

  private static JPanel labeledField(String text, JTextField field) {
    JPanel panel = new JPanel(new BorderLayout(0, 3));
    JLabel label = new JLabel(text);
    label.setLabelFor(field);
    panel.add(label, BorderLayout.NORTH);
    panel.add(field, BorderLayout.CENTER);
    return panel;
  }

  private void resetOnCriteriaChange(SearchJobsViewModel viewModel) {
    Runnable reset =
        () -> {
          if (!viewModel.getState().isLoading()) {
            viewModel.setState(
                new SearchJobsState(
                    List.of(), "Press Search to see matching internships.", "", false, false));
          }
        };
    DocumentListener listener =
        new DocumentListener() {
          @Override
          public void insertUpdate(DocumentEvent event) {
            reset.run();
          }

          @Override
          public void removeUpdate(DocumentEvent event) {
            reset.run();
          }

          @Override
          public void changedUpdate(DocumentEvent event) {
            reset.run();
          }
        };
    query.getDocument().addDocumentListener(listener);
    location.getDocument().addDocumentListener(listener);
    company.getDocument().addDocumentListener(listener);
  }

  private void addAttribution(LinkOpener opener) {
    model.addColumn("Source");
    int sourceIndex = model.findColumn("Source");
    TableColumn column = table.getColumnModel().getColumn(sourceIndex);
    column.setMinWidth(118);
    DefaultTableCellRenderer renderer =
        new DefaultTableCellRenderer() {
          @Override
          protected void setValue(Object value) {
            setText("<html><u>" + source.label + "</u></html>");
          }
        };
    renderer.setHorizontalAlignment(SwingConstants.CENTER);
    renderer.setToolTipText("Open " + source.label + " Canada (Enter on this cell)");
    column.setCellRenderer(renderer);
    table.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent event) {
            int col = table.columnAtPoint(event.getPoint());
            if (SwingUtilities.isLeftMouseButton(event)
                && col >= 0
                && table.rowAtPoint(event.getPoint()) >= 0
                && table.convertColumnIndexToModel(col) == sourceIndex) {
              openAttribution(opener);
            }
          }
        });
    table.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "openSelectedLink");
    table
        .getActionMap()
        .put(
            "openSelectedLink",
            new AbstractAction() {
              @Override
              public void actionPerformed(ActionEvent event) {
                int col = table.getSelectedColumn();
                if (table.getSelectedRow() >= 0
                    && col >= 0
                    && table.convertColumnIndexToModel(col) == sourceIndex) {
                  openAttribution(opener);
                } else {
                  open.doClick();
                }
              }
            });
  }

  private void openAttribution(LinkOpener opener) {
    try {
      opener.open(source.homepage);
    } catch (IOException | RuntimeException exception) {
      status.setText("The browser could not open " + source.homepage);
    }
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
    JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEADING, 8, 0));
    actions.add(open);
    if (source == Source.JOOBLE) {
      actions.add(more);
    }
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
    // Starting a new query clears the table; subsequent batches only append rows.
    rows = state.getRows();
    if (rows.isEmpty()) {
      model.setRowCount(0);
    }
    for (int index = model.getRowCount(); index < rows.size(); index++) {
      SearchJobsState.Row row = rows.get(index);
      if (source != Source.BOARDS) {
        model.addRow(
            new Object[] {row.getTitle(), row.getCompany(), row.getLocation(), source.label});
      } else {
        model.addRow(new Object[] {row.getTitle(), row.getCompany(), row.getLocation()});
      }
    }
    query.setEnabled(!state.isLoading());
    search.setEnabled(!state.isLoading());
    company.setEnabled(!state.isLoading());
    more.setEnabled(state.hasMore() && !state.isLoading());
    if (companies != null) {
      companies.setSearchEnabled(!state.isLoading());
    }
    location.setEnabled(!state.isLoading());
    status.setText(state.getMessage());
    status.setForeground(state.isError() ? new Color(160, 30, 30) : getForeground());
    warning.setText(state.getWarning());
    warning.setVisible(!state.getWarning().isEmpty());
    updateSelection();
  }

  private void updateSelection() {
    int selected = table.getSelectedRow();
    int index = selected < 0 ? -1 : table.convertRowIndexToModel(selected);
    boolean valid = index >= 0 && index < rows.size();
    open.setEnabled(valid);
    link.setText(valid ? rows.get(index).getApplicationUrl() : "");
  }
}
