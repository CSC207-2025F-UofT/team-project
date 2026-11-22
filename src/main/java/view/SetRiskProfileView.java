package view;

import interfaceadapter.setriskprofile.SetRiskProfileController;
import interfaceadapter.setriskprofile.SetRiskProfileState;
import interfaceadapter.setriskprofile.SetRiskProfileViewModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

/**
 * The view for the Set Risk Profile use case. Lets the user choose a risk profile (a preset or
 * custom values) and a past price window, then shows the estimated sell date and return.
 */
public class SetRiskProfileView extends JPanel implements PropertyChangeListener {
  private static final String CUSTOM_LABEL = "Custom";
  private static final double MIN_GAIN_PERCENT = 0.01;
  private static final double MAX_GAIN_PERCENT = 1000.0;
  private static final int MAX_HOLDING_DAYS = 5040;

  private final SetRiskProfileViewModel viewModel;
  private SetRiskProfileController controller;

  private final JTextField tickerField = new JTextField(10);
  private final JSpinner amountSpinner =
          new JSpinner(new SpinnerNumberModel(1000.0, 0.01, 1e9, 100.0));
  private final JComboBox<String> profileComboBox = new JComboBox<>();
  private final JSpinner targetGainSpinner = new JSpinner(
          new SpinnerNumberModel(10.0, MIN_GAIN_PERCENT, MAX_GAIN_PERCENT, 0.5));
  private final JSpinner holdingPeriodSpinner =
          new JSpinner(new SpinnerNumberModel(252, 1, MAX_HOLDING_DAYS, 1));
  private final JComboBox<String> windowComboBox = new JComboBox<>();
  private final JButton estimateButton = new JButton("Estimate");

  private final JLabel statusLabel = new JLabel(" ");
  private final JPanel resultPanel = new JPanel(new GridBagLayout());
  private final JLabel resultTitleLabel = new JLabel();
  private final JLabel buyPriceLabel = new JLabel();
  private final JLabel sellDateLabel = new JLabel();
  private final JLabel sellPriceLabel = new JLabel();
  private final JLabel priceRangeLabel = new JLabel();
  private final JLabel returnsLabel = new JLabel();
  private final JLabel targetMessageLabel = new JLabel();

  /** Set while the view is being updated from the view model, so listeners don't react. */
  private boolean updatingFromModel;

  /**
   * Creates the Set Risk Profile view.
   *
   * @param viewModel The view model this view displays
   */
  public SetRiskProfileView(SetRiskProfileViewModel viewModel) {
    this.viewModel = viewModel;
    viewModel.addPropertyChangeListener(this);

    setLayout(new BorderLayout(0, 12));
    setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

    JLabel title = new JLabel("Risk Profile & Sell Estimate");
    title.setFont(title.getFont().deriveFont(Font.BOLD, 18f));
    add(title, BorderLayout.NORTH);

    add(buildForm(), BorderLayout.CENTER);
    add(buildResults(), BorderLayout.SOUTH);

    for (Integer months : SetRiskProfileViewModel.WINDOW_OPTIONS) {
      windowComboBox.addItem("Past " + SetRiskProfileViewModel.formatMonths(months));
    }
    amountSpinner.setEditor(new JSpinner.NumberEditor(amountSpinner, "#,##0.00"));
    targetGainSpinner.setEditor(new JSpinner.NumberEditor(targetGainSpinner, "0.##"));

    profileComboBox.addActionListener(e -> onProfileSelected());
    estimateButton.addActionListener(e -> onEstimate());
    tickerField.addActionListener(e -> onEstimate());
  }

  public String getViewName() {
    return viewModel.getViewName();
  }

  public void setController(SetRiskProfileController controller) {
    this.controller = controller;
  }

  private JPanel buildForm() {
    JPanel form = new JPanel(new GridBagLayout());
    int row = 0;
    addRow(form, row++, "Ticker:", tickerField);
    addRow(form, row++, "Amount to invest ($):", amountSpinner);
    addRow(form, row++, "Risk profile:", profileComboBox);
    addRow(form, row++, "Target gain (%):", targetGainSpinner);
    addRow(form, row++, "Max holding period (trading days):", holdingPeriodSpinner);
    addRow(form, row++, "Estimate from:", windowComboBox);

    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 1;
    c.gridy = row++;
    c.anchor = GridBagConstraints.WEST;
    c.insets = new Insets(8, 4, 4, 4);
    form.add(estimateButton, c);

    c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = row;
    c.gridwidth = 2;
    c.anchor = GridBagConstraints.WEST;
    c.insets = new Insets(4, 4, 4, 4);
    form.add(statusLabel, c);
    return form;
  }

  private JPanel buildResults() {
    resultPanel.setBorder(BorderFactory.createTitledBorder("Estimate"));
    resultTitleLabel.setFont(resultTitleLabel.getFont().deriveFont(Font.BOLD));
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = 0;
    c.gridwidth = 2;
    c.anchor = GridBagConstraints.WEST;
    c.insets = new Insets(4, 4, 4, 4);
    resultPanel.add(resultTitleLabel, c);

    int row = 1;
    addRow(resultPanel, row++, "Buy price:", buyPriceLabel);
    addRow(resultPanel, row++, "Sell on:", sellDateLabel);
    addRow(resultPanel, row++, "Projected sell price:", sellPriceLabel);
    addRow(resultPanel, row++, "Likely price range (±1σ):", priceRangeLabel);
    addRow(resultPanel, row++, "Projected return:", returnsLabel);

    c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = row;
    c.gridwidth = 2;
    c.anchor = GridBagConstraints.WEST;
    c.insets = new Insets(4, 4, 4, 4);
    resultPanel.add(targetMessageLabel, c);

    resultPanel.setVisible(false);
    return resultPanel;
  }

  private static void addRow(JPanel panel, int row, String label, JComponent field) {
    GridBagConstraints c = new GridBagConstraints();
    c.gridx = 0;
    c.gridy = row;
    c.anchor = GridBagConstraints.WEST;
    c.insets = new Insets(4, 4, 4, 12);
    panel.add(new JLabel(label), c);

    c = new GridBagConstraints();
    c.gridx = 1;
    c.gridy = row;
    c.anchor = GridBagConstraints.WEST;
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 1.0;
    c.insets = new Insets(4, 4, 4, 4);
    panel.add(field, c);
  }

  /**
   * Fills in the preset's values when a preset is chosen, and only allows editing the values
   * when "Custom" is chosen.
   */
  private void onProfileSelected() {
    if (updatingFromModel) {
      return;
    }
    List<SetRiskProfileState.PresetOption> presets = viewModel.getState().getPresets();
    int index = profileComboBox.getSelectedIndex();
    boolean custom = index < 0 || index >= presets.size();
    if (!custom) {
      SetRiskProfileState.PresetOption preset = presets.get(index);
      setGainAndHolding(preset.getTargetGainPercent(), preset.getMaxHoldingPeriod());
    }
    targetGainSpinner.setEnabled(custom);
    holdingPeriodSpinner.setEnabled(custom);
  }

  private void onEstimate() {
    if (controller == null || !estimateButton.isEnabled()) {
      return;
    }
    commitSpinnerEdits();

    String ticker = tickerField.getText();
    double amount = ((Number) amountSpinner.getValue()).doubleValue();
    double targetGainPercent = ((Number) targetGainSpinner.getValue()).doubleValue();
    int maxHoldingPeriod = ((Number) holdingPeriodSpinner.getValue()).intValue();
    int windowMonths = SetRiskProfileViewModel.WINDOW_OPTIONS
            .get(Math.max(0, windowComboBox.getSelectedIndex()));

    estimateButton.setEnabled(false);
    setStatus("Fetching price data…", false);

    // Run the use case off the Event Dispatch Thread so the UI stays responsive during the
    // network call.
    new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() {
        controller.execute(ticker, amount, targetGainPercent, maxHoldingPeriod, windowMonths);
        return null;
      }

      @Override
      protected void done() {
        estimateButton.setEnabled(true);
        if ("Fetching price data…".equals(statusLabel.getText())) {
          setStatus(" ", false);
        }
        try {
          get();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
          setStatus("Unexpected error: " + e.getCause().getMessage(), true);
        }
      }
    }.execute();
  }

  /** Commits any text typed into the spinners that hasn't been committed yet. */
  private void commitSpinnerEdits() {
    for (JSpinner spinner : List.of(amountSpinner, targetGainSpinner, holdingPeriodSpinner)) {
      try {
        spinner.commitEdit();
      } catch (ParseException e) {
        // Invalid text is discarded and the spinner keeps its last valid value.
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    // The presenter may update the view model from a background thread; Swing components must
    // only be touched on the Event Dispatch Thread.
    if (!SwingUtilities.isEventDispatchThread()) {
      SwingUtilities.invokeLater(() -> propertyChange(evt));
      return;
    }

    SetRiskProfileState state = (SetRiskProfileState) evt.getNewValue();
    switch (evt.getPropertyName()) {
      case SetRiskProfileViewModel.SETTINGS_PROPERTY:
        showSettings(state);
        break;
      case SetRiskProfileViewModel.RESULT_PROPERTY:
        showResult(state);
        break;
      case SetRiskProfileViewModel.ERROR_PROPERTY:
        setStatus(state.getErrorMessage(), true);
        break;
      default:
        break;
    }
  }

  private void showSettings(SetRiskProfileState state) {
    updatingFromModel = true;
    try {
      profileComboBox.removeAllItems();
      for (SetRiskProfileState.PresetOption preset : state.getPresets()) {
        profileComboBox.addItem(preset.getLabel());
      }
      profileComboBox.addItem(CUSTOM_LABEL);

      int presetIndex = state.getSelectedPresetIndex();
      boolean custom = presetIndex < 0;
      profileComboBox.setSelectedIndex(custom ? state.getPresets().size() : presetIndex);
      setGainAndHolding(state.getTargetGainPercent(), state.getMaxHoldingPeriod());
      targetGainSpinner.setEnabled(custom);
      holdingPeriodSpinner.setEnabled(custom);

      int windowIndex = SetRiskProfileViewModel.WINDOW_OPTIONS.indexOf(state.getWindowMonths());
      if (windowIndex < 0) {
        windowIndex = SetRiskProfileViewModel.WINDOW_OPTIONS.indexOf(12);
      }
      windowComboBox.setSelectedIndex(windowIndex);
    } finally {
      updatingFromModel = false;
    }
  }

  private void showResult(SetRiskProfileState state) {
    resultTitleLabel.setText("Estimate for " + state.getResultTicker());
    buyPriceLabel.setText(state.getBuyPrice());
    sellDateLabel.setText(state.getSellDate());
    sellPriceLabel.setText(state.getSellPrice());
    priceRangeLabel.setText(state.getPriceRange());
    returnsLabel.setText(state.getReturns());
    targetMessageLabel.setText(state.getTargetMessage());
    targetMessageLabel.setForeground(state.isTargetReached()
            ? new Color(0, 128, 0) : new Color(176, 112, 0));
    resultPanel.setVisible(true);
    setStatus(" ", false);
    revalidateWindow();
  }

  private void setGainAndHolding(double targetGainPercent, int maxHoldingPeriod) {
    targetGainSpinner.setValue(
            Math.min(MAX_GAIN_PERCENT, Math.max(MIN_GAIN_PERCENT, targetGainPercent)));
    holdingPeriodSpinner.setValue(Math.min(MAX_HOLDING_DAYS, Math.max(1, maxHoldingPeriod)));
  }

  private void setStatus(String message, boolean isError) {
    statusLabel.setText(message == null || message.isEmpty() ? " " : message);
    statusLabel.setForeground(isError ? Color.RED : UIManager.getColor("Label.foreground"));
    revalidateWindow();
  }

  private void revalidateWindow() {
    revalidate();
    Window window = SwingUtilities.getWindowAncestor(this);
    if (window != null) {
      window.pack();
    }
  }
}
