package view;

import use_case.ai_insights.GeminiClient;
import use_case.ai_insights.InsightService;
import entity.SpendingSummary;
import controller.InsightsController;
import use_case.ai_insights.presenter.InsightPresenter;
import interface_adapter.InsightViewModel;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class InsightView extends JFrame {

    public InsightView() {

        JLabel spendingSummaryLabel = new JLabel("<html>Loading summary...</html>");
        JLabel resultLabel = new JLabel("<html>Press the button to generate insights on your spending pattern.</html>");

        JButton generateButton = new JButton("Generate AI Insight");

        spendingSummaryLabel.setPreferredSize(new Dimension(550, 100));
        resultLabel.setPreferredSize(new Dimension(550, 250));

        spendingSummaryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        InsightViewModel vm = new InsightViewModel();
        InsightPresenter presenter = new InsightPresenter(vm);
        InsightService interactor = new InsightService(new GeminiClient());
        InsightsController controller = new InsightsController(interactor, presenter);

        // temp spending data
        SpendingSummary summary = new SpendingSummary(
                560.75,
                Map.of(
                        "Dining", 210.50,
                        "Groceries", 320.25,
                        "Shopping", 30.00
                ),
                "Groceries"
        );

        presenter.presentSummary(summary);
        spendingSummaryLabel.setText("<html>" + vm.getSpendingBreakdown().replace("\n", "<br>") + "</html>");

        generateButton.addActionListener(e -> {
            controller.generateInsight(summary, "demoUser");
            String formattedOutput = "<html>" +
                    vm.getSummary().replace("\n", "<br>") +
                    "<br><br><b>Tips:</b><br>" +
                    vm.getRecommendations().replace("\n", "<br>") +
                    "<br><br>" + vm.getDate() +
                    "</html>";

            resultLabel.setText(formattedOutput);
        });

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(spendingSummaryLabel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(generateButton);
        panel.add(Box.createVerticalStrut(10));
        panel.add(resultLabel);

        setContentPane(panel);
        setTitle("AI Spending Insights");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
}
