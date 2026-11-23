package view;

import entities.DailyAggregate;
import interfaceadapter.viewstockinfo.ViewStockInfoController;
import interfaceadapter.viewstockinfo.ViewStockInfoState;
import interfaceadapter.viewstockinfo.ViewStockInfoViewModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Paint;
import java.beans.PropertyChangeEvent;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.HighLowItemLabelGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.data.xy.DefaultOHLCDataset;
import org.jfree.data.xy.OHLCDataItem;
import usecase.viewstockinfo.PricePoint;
import usecase.viewstockinfo.ViewStockInfoOutputData;

/** Shows a stock's end-of-day table and a year of weekly candlesticks. */
public class ViewStockInfoView extends JPanel {
  private static final Color UP_CANDLE = new Color(0, 128, 96);
  private static final Color DOWN_CANDLE = new Color(190, 55, 64);
  private static final Color FLAT_CANDLE = new Color(85, 93, 104);
  private static final int MAX_CANDLE_WIDTH_DAYS = 4;

  private final ViewStockInfoController controller;
  private final JPanel watchlistRows = new JPanel();
  private final List<JButton> viewButtons = new ArrayList<>();
  private final JLabel statusLabel = new JLabel(" ");
  private final ChartPanel chartPanel = new ChartPanel(null, false);
  private boolean lookupInProgress;
  private final DefaultTableModel tableModel = new DefaultTableModel(
          new String[] {"Date", "Latest close", "Change", "Change %", "High", "Low", "Volume"}, 0) {
    @Override
    public Class<?> getColumnClass(int columnIndex) {
      if (columnIndex == 0) {
        return LocalDate.class;
      }
      return columnIndex == 2 || columnIndex == 3 ? String.class : Double.class;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
      return false;
    }
  };

  /** Creates the stock information view and connects it to the lookup controller. */
  public ViewStockInfoView(ViewStockInfoViewModel viewModel, ViewStockInfoController controller) {
    this.controller = controller;
    viewModel.addPropertyChangeListener(this::onStateChanged);

    setLayout(new BorderLayout(0, 12));
    setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

    watchlistRows.setLayout(new BoxLayout(watchlistRows, BoxLayout.Y_AXIS));
    JScrollPane watchlistPane = new JScrollPane(watchlistRows);
    watchlistPane.setPreferredSize(new Dimension(220, 320));
    watchlistPane.setBorder(BorderFactory.createTitledBorder("Watchlist"));
    add(watchlistPane, BorderLayout.WEST);
    add(statusLabel, BorderLayout.NORTH);

    chartPanel.setPreferredSize(new Dimension(700, 280));
    chartPanel.setPopupMenu(null);

    JTable table = new JTable(tableModel);
    table.setFillsViewportHeight(true);
    table.setPreferredScrollableViewportSize(new Dimension(700, table.getRowHeight() + 8));
    JScrollPane tablePane = new JScrollPane(table);
    tablePane.setBorder(BorderFactory.createTitledBorder("Latest end-of-day data"));
    JPanel details = new JPanel(new BorderLayout(0, 12));
    details.add(chartPanel, BorderLayout.CENTER);
    details.add(tablePane, BorderLayout.SOUTH);
    add(details, BorderLayout.CENTER);
  }

  private void onLookup(String ticker) {
    if (lookupInProgress) {
      return;
    }
    setLookupInProgress(true);
    tableModel.setRowCount(0);
    chartPanel.setChart(null);
    setStatus("Loading " + ticker + " data…", false);

    new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() {
        controller.execute(ticker);
        return null;
      }

      @Override
      protected void done() {
        try {
          get();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          setStatus("Stock lookup was interrupted.", true);
        } catch (ExecutionException e) {
          setStatus("An unexpected error prevented the stock lookup.", true);
        } finally {
          setLookupInProgress(false);
        }
      }
    }.execute();
  }

  private void setLookupInProgress(boolean inProgress) {
    lookupInProgress = inProgress;
    for (JButton button : viewButtons) {
      button.setEnabled(!inProgress);
    }
  }

  private void onStateChanged(PropertyChangeEvent event) {
    if (!SwingUtilities.isEventDispatchThread()) {
      SwingUtilities.invokeLater(() -> onStateChanged(event));
      return;
    }

    ViewStockInfoState state = (ViewStockInfoState) event.getNewValue();
    if (ViewStockInfoViewModel.WATCHLIST_PROPERTY.equals(event.getPropertyName())) {
      showWatchlist(state);
    } else if (ViewStockInfoViewModel.RESULT_PROPERTY.equals(event.getPropertyName())) {
      showResult(state.getResult(), state.getErrorMessage());
    }
  }

  private void showWatchlist(ViewStockInfoState state) {
    watchlistRows.removeAll();
    viewButtons.clear();
    if (state.getWatchlistErrorMessage() != null) {
      watchlistRows.add(new JLabel("Could not load watchlist."));
      JButton retry = new JButton("Retry");
      retry.addActionListener(event -> controller.loadWatchlist());
      watchlistRows.add(retry);
    } else if (state.getTickers().isEmpty()) {
      watchlistRows.add(new JLabel("Your watchlist is empty."));
    } else {
      for (String ticker : state.getTickers()) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEADING, 8, 3));
        row.setAlignmentX(Component.LEFT_ALIGNMENT);
        row.add(new JLabel(ticker));
        JButton viewButton = new JButton("View");
        viewButton.getAccessibleContext().setAccessibleName("View " + ticker);
        viewButton.addActionListener(event -> onLookup(ticker));
        viewButton.setEnabled(!lookupInProgress);
        viewButtons.add(viewButton);
        row.add(viewButton);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, row.getPreferredSize().height));
        watchlistRows.add(row);
      }
    }
    watchlistRows.revalidate();
    watchlistRows.repaint();
  }

  private void showResult(ViewStockInfoOutputData result, String errorMessage) {
    tableModel.setRowCount(0);
    if (result == null) {
      chartPanel.setChart(null);
      setStatus(errorMessage, true);
      return;
    }

    List<DailyAggregate> days = result.getDailyAggregates();
    List<PricePoint> prices = result.getPriceHistory();
    int last = days.size() - 1;
    DailyAggregate day = days.get(last);
    LocalDate latest = prices.get(last).getDate();
    tableModel.addRow(new Object[] {latest, day.getClose(),
        formatChange(result.getDailyChange(), false),
        formatChange(result.getDailyChangePercent(), true),
        day.getHigh(), day.getLow(), day.getVolume()});
    chartPanel.setChart(createChart(result));
    chartPanel.setMouseZoomable(false);
    if (result.isFromCache()) {
      setStatus("Current data is unavailable. Showing out-of-date " + result.getTicker()
              + " data through " + latest + ". Try again.", true);
    } else {
      setStatus(" ", false);
    }
  }

  private static String formatChange(Double value, boolean percentage) {
    if (value == null) {
      return "—";
    }
    return String.format(Locale.US, percentage ? "%+.2f%%" : "%+.2f", value);
  }

  private static JFreeChart createChart(ViewStockInfoOutputData result) {
    DefaultOHLCDataset dataset = new DefaultOHLCDataset(result.getTicker(),
            groupIntoWeeks(result.getDailyAggregates(), result.getPriceHistory()));
    JFreeChart chart = ChartFactory.createCandlestickChart(
            result.getTicker() + " · 1-year weekly candlesticks", "Last trading day of week",
            "Price", dataset, false);
    XYPlot plot = chart.getXYPlot();
    plot.setBackgroundPaint(Color.WHITE);
    plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
    plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

    DateAxis axis = (DateAxis) plot.getDomainAxis();
    TimeZone chartTimeZone = TimeZone.getTimeZone("UTC");
    axis.setTimeZone(chartTimeZone);
    // Pad the year by half a candle so that a partial week is visible at either edge.
    int edgePaddingDays = MAX_CANDLE_WIDTH_DAYS / 2;
    axis.setRange(chartTime(result.getFrom().minusDays(edgePaddingDays)),
            chartTime(result.getTo().plusDays(edgePaddingDays + 1)));

    NumberAxis priceAxis = (NumberAxis) plot.getRangeAxis();
    priceAxis.setAutoRangeIncludesZero(false);

    // Use the per-candle paint for the body, outline, and both wicks.
    CandlestickRenderer renderer = new CandlestickRenderer() {
      @Override
      public Paint getItemPaint(int series, int item) {
        double open = dataset.getOpenValue(series, item);
        double close = dataset.getCloseValue(series, item);
        if (close == open) {
          return FLAT_CANDLE;
        }
        return close > open ? UP_CANDLE : DOWN_CANDLE;
      }
    };
    renderer.setUpPaint(null);
    renderer.setDownPaint(null);
    renderer.setDrawVolume(false);
    renderer.setMaxCandleWidthInMilliseconds(Duration.ofDays(MAX_CANDLE_WIDTH_DAYS).toMillis());
    DateFormat tooltipDate = DateFormat.getDateInstance();
    tooltipDate.setTimeZone(chartTimeZone);
    renderer.setDefaultToolTipGenerator(new HighLowItemLabelGenerator(
            tooltipDate, NumberFormat.getNumberInstance()));
    plot.setRenderer(renderer);
    return chart;
  }

  private static OHLCDataItem[] groupIntoWeeks(List<DailyAggregate> days, List<PricePoint> prices) {
    List<OHLCDataItem> weeks = new ArrayList<>();
    int first = 0;
    while (first < days.size()) {
      DailyAggregate openingDay = days.get(first);
      LocalDate weekEnd = prices.get(first).getDate().with(TemporalAdjusters.nextOrSame(
              DayOfWeek.SUNDAY));
      double high = openingDay.getHigh();
      double low = openingDay.getLow();
      double volume = openingDay.getVolume();
      int last = first;
      while (last + 1 < days.size() && !prices.get(last + 1).getDate().isAfter(weekEnd)) {
        last++;
        DailyAggregate day = days.get(last);
        high = Math.max(high, day.getHigh());
        low = Math.min(low, day.getLow());
        volume += day.getVolume();
      }
      Date date = new Date(chartTime(prices.get(last).getDate()));
      weeks.add(new OHLCDataItem(date, openingDay.getOpen(), high, low,
              days.get(last).getClose(), volume));
      first = last + 1;
    }
    return weeks.toArray(new OHLCDataItem[0]);
  }

  private static long chartTime(LocalDate date) {
    return date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
  }

  private void setStatus(String message, boolean error) {
    statusLabel.setForeground(error ? Color.RED.darker() : UIManager.getColor("Label.foreground"));
    statusLabel.setText(message);
  }
}
