package view;

import interfaceadapter.estimateroi.EstimateROIController;
import interfaceadapter.estimateroi.EstimateROIState;
import interfaceadapter.estimateroi.EstimateROIViewModel;
import java.awt.FlowLayout;
import javax.swing.*;
import usecase.estimateroi.EstimateROIOutputData;

/** Lets the user estimate a 1-year return on the stock being viewed. */
public class EstimateROIView extends JPanel {
    private final JTextField amountField = new JTextField(8);
    private final JComboBox<Integer> historyBox = new JComboBox<>(new Integer[] {1, 3, 5});
    private final JButton estimateButton = new JButton("Estimate");
    private final JLabel resultLabel = new JLabel(" ");
    private String ticker;   // set when the user views a stock

    public EstimateROIView(EstimateROIViewModel viewModel, EstimateROIController controller) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Estimate return (1 year)"));

        JPanel inputs = new JPanel(new FlowLayout(FlowLayout.LEFT));
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
        estimateButton.addActionListener(e -> {
            double amount;
            try {
                amount = Double.parseDouble(amountField.getText().trim());
            } catch (NumberFormatException ex) {
                amount = -1;   // let the interactor produce the 2a message
            }
            final double a = amount;
            final int years = (Integer) historyBox.getSelectedItem();
            new SwingWorker<Void, Void>() {    // API call off the UI thread, like ViewStockInfoView
                @Override protected Void doInBackground() {
                    controller.execute(ticker, a, years);
                    return null;
                }
            }.execute();
        });
        historyBox.addActionListener(e -> {   // steps 5-7: re-estimate when history changes
            if (viewModel.getState().getResult() != null) {
                estimateButton.doClick();
            }
        });

        viewModel.addPropertyChangeListener(evt ->
                SwingUtilities.invokeLater(() -> show(viewModel.getState())));
    }

    /** Called when the user views a different stock. */
    public void setTicker(String ticker) {
        this.ticker = ticker;
        estimateButton.setEnabled(ticker != null);
        resultLabel.setText(" ");
    }

    private void show(EstimateROIState state) {
        EstimateROIOutputData r = state.getResult();
        String text = r == null ? "" : String.format(
                "Estimated value: $%.2f   Profit/loss: $%.2f (%.2f%%)   Based on %d year(s) of history",
                r.getEstimatedValue(), r.getProfit(), r.getPercentageReturn(), r.getHistoryLength());
        if (state.getErrorMessage() != null) {
            text = "<html>" + text + "<br><font color='red'>" + state.getErrorMessage() + "</font></html>";
        }
        resultLabel.setText(text.isEmpty() ? " " : text);
    }
}