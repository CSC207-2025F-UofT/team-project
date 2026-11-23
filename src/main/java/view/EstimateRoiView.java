package view;

import interfaceadapter.estimateroi.EstimateRoiController;
import interfaceadapter.estimateroi.EstimateRoiState;
import interfaceadapter.estimateroi.EstimateRoiViewModel;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import usecase.estimateroi.EstimateRoiOutputData;

/**
 * Lets the user estimate the one-year return of an investment in the stock being viewed.
 */
public class EstimateRoiView extends JPanel {
  private static final Integer[] HISTORY_OPTIONS = {1, 3, 5};

  private final EstimateRoiController controller;
  private final JTextField amountField = new JTextField(8);
  private final JComboBox<Integer> historyBox = new JComboBox<>(HISTORY_OPTIONS);
  private final JButton estimateButton = new JButton("Estimate");
  private final JLabel resultLabel = new JLabel(" ");
  private String ticker;

  /**
   * Creates the Estimate ROI view and connects it to its view model and controller.
   *
   * @param viewModel The view model holding the estimate to show
   * @param controller The controller used to request estimates
   */
  public EstimateRoiView(EstimateRoiViewModel viewModel, EstimateRoiController controller) {
    this.controller = controller;
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(BorderFactory.createTitledBorder("Estimate return (1 year)"));

    final JPanel inputs = new JPanel(new FlowLayout(FlowLayout.LEFT));
    inputs.add(new JLabel("Amount $"));
    inputs.add(amountField);
    inputs.add(new JLabel("Based on last"));
    inputs.add(historyBox);
    inputs.add(new JLabel("year(s)"));
    inputs.add(estimateButton);
    add(inputs);
    add(resultLabel);
    add(new JLabel("Estimate is based on past performance and is not financial advice."));

    estimateButton.setEnabled(false);
    estimateButton.addActionListener(event -> requestEstimate());
    historyBox.addActionListener(event -> {
      // Steps 5-7: produce a new estimate when the user picks another history length.
      if (viewModel.getState().getResult() != null) {
        requestEstimate();
      }
    });

    viewModel.addPropertyChangeListener(event ->
            SwingUtilities.invokeLater(() -> displayState(viewModel.getState())));
  }

  /**
   * Sets the stock that estimates are made for. Called when the user views a different stock.
   *
   * @param ticker The ticker of the stock being viewed, or null if none is selected
   */
  public void setTicker(String ticker) {
    this.ticker = ticker;
    estimateButton.setEnabled(ticker != null);
    resultLabel.setText(" ");
  }

  private void requestEstimate() {
    double parsedAmount;
    try {
      parsedAmount = Double.parseDouble(amountField.getText().trim());
    } catch (NumberFormatException exception) {
      // An invalid number is passed on so the interactor can explain what is expected.
      parsedAmount = -1;
    }
    final double amount = parsedAmount;
    final int historyYears = (Integer) historyBox.getSelectedItem();
    final String selectedTicker = ticker;

    // Run the API call off the Swing thread, as ViewStockInfoView does.
    new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() {
        controller.execute(selectedTicker, amount, historyYears);
        return null;
      }
    }.execute();
  }

  private void displayState(EstimateRoiState state) {
    final EstimateRoiOutputData result = state.getResult();
    String text = "";
    if (result != null) {
      text = String.format("Estimated value: $%.2f   Profit/loss: $%.2f (%.2f%%)   "
              + "Based on %d year(s) of history",
              result.getEstimatedValue(), result.getProfit(), result.getPercentageReturn(),
              result.getHistoryLength());
    }
    if (state.getErrorMessage() != null) {
      text = "<html>" + text + "<br><font color='red'>" + state.getErrorMessage()
              + "</font></html>";
    }
    if (text.isEmpty()) {
      text = " ";
    }
    resultLabel.setText(text);
  }
}
