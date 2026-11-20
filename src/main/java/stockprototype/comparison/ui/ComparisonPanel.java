package stockprototype.comparison.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableModel;
import stockprototype.comparison.CompareStocks;
import stockprototype.comparison.ComparisonResult;

public final class ComparisonPanel extends JPanel implements ComparisonPresenter.View {
    private static final long serialVersionUID = 1L;
    private enum Window {
        ALL("All available", 0), TWENTY("Last 20 shared dates", 20), SIXTY("Last 60 shared dates", 60);

        private final String label;
        private final int limit;

        Window(String label, int limit) {
            this.label = label;
            this.limit = limit;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private final JTextField firstSymbol = new JTextField("IBM", 8);
    private final JTextField secondSymbol = new JTextField("AAPL", 8);
    private final JComboBox<Window> window = new JComboBox<>(Window.values());
    private final JButton compare = new JButton("Compare");
    private final JLabel status = new JLabel("Select two symbols and click Compare.");
    private final CardLayout cards = new CardLayout();
    private final JPanel body = new JPanel(cards);
    private final JLabel period = new JLabel();
    private final JLabel performance = new JLabel();
    private final JLabel correlation = new JLabel();
    private final JTable summary = new JTable();
    private final JTable history = new JTable();
    private final IndexedPriceChart chart = new IndexedPriceChart();
    private final ComparisonController controller;
    private final boolean live;

    public ComparisonPanel(CompareStocks useCase, boolean live) {
        super(new BorderLayout(0, 10));
        this.live = live;
        controller = new ComparisonController(useCase, new ComparisonPresenter(this));
        setBorder(BorderFactory.createEmptyBorder(14, 16, 12, 16));
        setPreferredSize(new Dimension(840, 630));
        add(buildForm(live), BorderLayout.NORTH);
        body.add(new JPanel(), "empty");
        body.add(buildResults(), "results");
        add(body, BorderLayout.CENTER);
        compare.addActionListener(event -> submit());
        firstSymbol.addActionListener(event -> submit());
        secondSymbol.addActionListener(event -> submit());
    }

    private JPanel buildForm(boolean live) {
        JPanel form = new JPanel(new BorderLayout(0, 6));
        JLabel title = new JLabel("Compare two stocks");
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        JLabel source = new JLabel(live ? "Alpha Vantage · Historical daily closes"
                : "Sample data · Synthetic histories for IBM, AAPL and MSFT");
        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.add(title);
        heading.add(source);
        form.add(heading, BorderLayout.NORTH);

        JPanel fields = new JPanel(new FlowLayout(FlowLayout.LEADING, 6, 2));
        fields.add(labelFor("Stock 1", firstSymbol));
        fields.add(firstSymbol);
        fields.add(labelFor("Stock 2", secondSymbol));
        fields.add(secondSymbol);
        fields.add(labelFor("Period", window));
        fields.add(window);
        fields.add(compare);
        form.add(fields, BorderLayout.CENTER);
        form.add(status, BorderLayout.SOUTH);
        return form;
    }

    private static JLabel labelFor(String label, JComponent field) {
        JLabel component = new JLabel(label);
        component.setLabelFor(field);
        return component;
    }

    private JPanel buildResults() {
        JPanel results = new JPanel(new BorderLayout(0, 8));
        JPanel overview = new JPanel(new BorderLayout(0, 6));
        overview.add(period, BorderLayout.NORTH);
        JPanel summaryBlock = new JPanel(new BorderLayout());
        summary.setFillsViewportHeight(true);
        summary.setRowHeight(summary.getRowHeight() + 8);
        summary.setRowSelectionAllowed(false);
        summary.getTableHeader().setReorderingAllowed(false);
        summary.getAccessibleContext().setAccessibleName("Comparison summary");
        summaryBlock.add(summary.getTableHeader(), BorderLayout.NORTH);
        summaryBlock.add(summary, BorderLayout.CENTER);
        overview.add(summaryBlock, BorderLayout.CENTER);
        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.add(performance);
        details.add(correlation);
        details.add(new JLabel("Estimate: one more interval at the period’s compound growth rate."));
        overview.add(details, BorderLayout.SOUTH);
        results.add(overview, BorderLayout.NORTH);

        JTabbedPane data = new JTabbedPane();
        data.addTab("Performance chart", chart);
        history.getTableHeader().setReorderingAllowed(false);
        history.getAccessibleContext().setAccessibleName("Shared daily closing prices");
        history.setFillsViewportHeight(true);
        data.addTab("Closing prices", new JScrollPane(history));
        results.add(data, BorderLayout.CENTER);
        results.add(new JLabel("Raw closes in each stock’s quote currency. "
                + "Price changes exclude dividend and split adjustments."), BorderLayout.SOUTH);
        return results;
    }

    private void submit() {
        Window selected = (Window) window.getSelectedItem();
        controller.compare(firstSymbol.getText(), secondSymbol.getText(), selected.limit);
    }

    @Override
    public void startLoading() {
        setInputsEnabled(false);
        status.setText(live ? "Loading both stocks… Live requests are spaced 2.5 seconds apart."
                : "Loading sample prices…");
        cards.show(body, "empty");
    }

    @Override
    public void showError(String message) {
        status.setText("Comparison failed: " + message);
        cards.show(body, "empty");
        setInputsEnabled(true);
    }

    @Override
    public void showComparison(ComparisonResult result, String dateRange, String leader,
                               String returnCorrelation, TableModel summaryData, TableModel historyData) {
        status.setText("Comparison of " + result.firstSymbol + " and " + result.secondSymbol);
        period.setText(dateRange);
        performance.setText(leader);
        correlation.setText(returnCorrelation);
        summary.setModel(summaryData);
        history.setModel(historyData);
        chart.setResult(result);
        cards.show(body, "results");
        setInputsEnabled(true);
    }

    private void setInputsEnabled(boolean enabled) {
        firstSymbol.setEnabled(enabled);
        secondSymbol.setEnabled(enabled);
        window.setEnabled(enabled);
        compare.setEnabled(enabled);
    }
}
