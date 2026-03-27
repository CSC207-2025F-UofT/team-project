package use_case.ai_insights;
import entity.Insight;
import use_case.transaction_categorizer.GeminiClient;
import entity.SpendingSummary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class InsightPromptBuilder {

    public static String buildPrompt(SpendingSummary summary) {
        String highest = summary.highestCategory();
        if (highest == null || highest.isBlank()) {
            highest = "Unknown";
        }

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
            Total spent: $""" + summary.totalSpent() + "\n" +
            "Highest spending category: " + highest;
    }

}
