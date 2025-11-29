package use_case.ai_insights;

import entity.SpendingSummary;

import java.util.Map;

public class InsightPromptBuilder {

    public static String buildPrompt(SpendingSummary summary) {

        StringBuilder spendingLines = new StringBuilder();
        for (Map.Entry<String, Double> entry : summary.totals().entrySet()) {
            spendingLines.append(entry.getKey())
                    .append(": $")
                    .append(entry.getValue())
                    .append("\n");
        }

        return """
            You are a financial assistant. Analyze the user's spending.

            Your response MUST be **only valid JSON**, no extra commentary, no markdown, no backticks.

            JSON format (use only this):

            {
              "summary": "short high level insight",
              "tips": [
                "specific actionable advice",
                "another budgeting tip",
                "optional third tip"
              ]
            }

            Do NOT wrap the response in quotes, markdown, or explanations.

            User's spending data:
            """ + spendingLines + """
            Total spent: $""" + summary.totalSpent();
    }
}
