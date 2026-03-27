package use_case.ai_insights;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import entity.Insight;
import entity.SpendingSummary;

public class InsightService {

    private final InsightClient gemini;

    public InsightService(InsightClient gemini) {
        this.gemini = gemini;
    }

    public Insight generateInsights(SpendingSummary summary) {

        if (summary.totalSpent() == 0) {
            Insight i = new Insight();
            i.setGeneratedAt(LocalDate.now());
            i.setSummaryText("Not enough transaction data to generate insights.");
            i.setRecommendations(List.of("Add more spending history and try again."));
            return i;
        }

        String prompt = InsightPromptBuilder.buildPrompt(summary);

        String rawResponse = gemini.generateInsight(prompt);

        System.out.println("\nRAW RESPONSE >>>\n" + rawResponse + "\n<<< END\n");

        try {
            rawResponse = rawResponse.replace("```json", "").replace("```", "").
                    trim();

            JSONObject root = new JSONObject(rawResponse);

            String generatedText;

            if (root.has("candidates")) {
                generatedText = root
                        .getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")
                        .trim();
            } else {
                generatedText = rawResponse;
            }

            JSONObject json = new JSONObject(generatedText);

            String summaryText = json.optString("summary", "No summary found.");
            JSONArray tipsJson = json.optJSONArray("tips");

            List<String> recommendations = new ArrayList<>();
            if (tipsJson != null) {
                for (int i = 0; i < tipsJson.length(); i++) {
                    recommendations.add(tipsJson.getString(i));
                }
            }

            Insight insight = new Insight();
            insight.setGeneratedAt(LocalDate.now());
            insight.setSummaryText(summaryText);
            insight.setRecommendations(recommendations);

            return insight;

        } catch (Exception e) {
            Insight fallback = new Insight();
            fallback.setGeneratedAt(LocalDate.now());
            fallback.setSummaryText("Insight generation failed.");
            fallback.setRecommendations(List.of());
            return fallback;
        }
    }

}
