package view;

import use_case.ai_insights.*;
import entity.SpendingSummary;

import javax.swing.*;
import java.awt.*;

public class InsightView extends JPanel {

    private final JLabel resultLabel = new JLabel("<html>Press the button to generate insights.</html>");

    public InsightView(SpendingSummary summary) {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel spendingSummaryLabel = new JLabel("<html>Loading summary...</html>");
        JButton generateButton = new JButton("Generate AI Insight");

        spendingSummaryLabel.setPreferredSize(new Dimension(550, 100));
        resultLabel.setPreferredSize(new Dimension(550, 250));

        spendingSummaryLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        InsightViewModel vm = new InsightViewModel();
        InsightPresenter presenter = new InsightPresenter(vm);
        InsightService interactor = new InsightService(new InsightClient());
        InsightsController controller = new InsightsController(interactor, presenter);

        // Populate summary
        presenter.presentSummary(summary);
        spendingSummaryLabel.setText("<html>" + vm.getSpendingBreakdown().replace("\n", "<br>") + "</html>");

        generateButton.addActionListener(e -> {
            controller.generateInsight(summary, "demoUser");
            String formattedOutput = "<html>" +
                    "<b>Insight:</b><br>" + vm.getSummary().replace("\n", "<br>") +
                    "<br><br><b>Tips:</b><br>" + vm.getRecommendations().replace("\n", "<br>") +
                    "<br><br><i>" + vm.getDate() + "</i>" +
                    "</html>";

            resultLabel.setText(formattedOutput);
        });

        add(spendingSummaryLabel);
        add(Box.createVerticalStrut(10));
        add(generateButton);
        add(Box.createVerticalStrut(10));
        add(resultLabel);
    }
}
