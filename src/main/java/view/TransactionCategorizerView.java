package view;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import entity.SpendingSummary;
import entity.Transaction;
import interface_adapter.categorizer.CategorizerController;
import interface_adapter.categorizer.CategorizerViewModel;
import use_case.ai_insights.InsightClient;
import use_case.ai_insights.InsightPresenter;
import use_case.ai_insights.InsightService;
import use_case.ai_insights.InsightViewModel;
import use_case.ai_insights.InsightsController;
import use_case.ai_insights.TrendAnalyzer;

public class TransactionCategorizerView extends JPanel implements PropertyChangeListener {

    private final String viewName = "categorizer view";

    private final JTextArea insightArea = new JTextArea("Insights will appear here...");
    private final JTextArea resultsArea = new JTextArea();
    private final JButton categorizeButton = new JButton("Categorize Transactions");
    private final JButton insightButton = new JButton("Generate Insights");
    private final JButton viewReportButton = new JButton("View Report");
    private final JButton backButton = new JButton("Back");


    private final CategorizerViewModel vm;
    private CategorizerController categorizerController;

    public TransactionCategorizerView(CategorizerViewModel vm) {
        this.vm = vm;
        setLayout(new BorderLayout());
        resultsArea.setEditable(false);
        insightArea.setEditable(false);
        insightArea.setVisible(false);
        vm.addPropertyChangeListener(this);
        JPanel topPanel = new JPanel();
        topPanel.add(categorizeButton);
        topPanel.add(viewReportButton);
        topPanel.add(insightButton);
        topPanel.add(backButton);
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultsArea), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createTitledBorder("AI Insights"));
        bottomPanel.add(new JScrollPane(insightArea), BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        backButton.addActionListener(e -> {
            // Go back to upload statement screen
            categorizerController.goBackToUploadStatement();
        });


        categorizeButton.addActionListener(e ->
                categorizerController.categorizeTransactions(vm.getTransactions()));
        insightButton.addActionListener(e -> generateInsights());
        viewReportButton.addActionListener(e -> {
            categorizerController.goToSpendingReport(vm.getTransactions().get(0).getUsername());
        });
    }

    private void generateInsights() {
        if (vm.getTransactions() == null || vm.getTransactions().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Categorize transactions first.");
            return;
        }

        insightArea.setText("Generating insights...\nPlease wait.");
        insightArea.setVisible(true);

        TrendAnalyzer analyzer = new TrendAnalyzer();
        SpendingSummary summary = analyzer.analyze(vm.getTransactions());

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                InsightViewModel insightVM = new InsightViewModel();
                InsightPresenter presenter = new InsightPresenter(insightVM);
                InsightService interactor = new InsightService(new InsightClient());
                InsightsController controller = new InsightsController(interactor, presenter);

                controller.generateInsight(summary);

                String formatted = """
                    Insight:
                    %s
                    
                    Recommendations:
                    %s
                    
                    %s
                    """.formatted(
                        insightVM.getSummary(),
                        insightVM.getRecommendations(),
                        insightVM.getDate()
                );

                insightArea.setText(formatted);
                return null;
            }
        };

        worker.execute();
    }

    public String getViewName() { return viewName; }

    public void setCategorizerController(CategorizerController categorizerController) {
        this.categorizerController = categorizerController;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        StringBuilder sb = new StringBuilder("Categorized Transactions:\n\n");
        CategorizerViewModel viewModel = (CategorizerViewModel) evt.getSource();
        for (Transaction t : viewModel.getTransactions()) {
            sb.append(t.getDate())
                    .append(" | ")
                    .append(t.getMerchant())
                    .append(" | $")
                    .append(t.getAmount())
                    .append(" | Category: ")
                    .append(t.getCategory().toUpperCase())
                    .append("\n");
        }
        resultsArea.setText(sb.toString());
    }

}
