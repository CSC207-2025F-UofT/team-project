package use_case.transaction_categorizer;

import entity.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class TransactionCategorizerService {

    private final GeminiClient client;

    public TransactionCategorizerService(GeminiClient client) {
        this.client = client;
    }

    public void categorize(List<Transaction> transactions) {

        if (System.getenv("GEMINI_API_KEY") == null) {
            System.out.println("No Gemini API key detected: fallback mode.");
            transactions.forEach(t -> t.setCategory("ERRNOKEY"));
            return;
        }

        StringBuilder batchPrompt = new StringBuilder("""
                You are a classification engine.
                
                Categorize each transaction into EXACTLY one of the following categories:
                GROCERIES, SHOPPING, DINING OUT, ENTERTAINMENT, UTILITIES, TRANSPORTATION, FITNESS, MISCELLANEOUS.
                
                IMPORTANT:
                - Respond ONLY with VALID JSON.
                - NO explanations, no comments.
                - DO NOT add extra text.
                - Format MUST be:
                
                [
                  {"description": "...", "category": "SHOPPING"},
                  {"description": "...", "category": "DINING OUT"}
                ]
                
                Here are the transactions:
                """);

        for (Transaction t : transactions) {
            batchPrompt.append("- ").append(t.getMerchant())
                    .append(" ($").append(t.getAmount()).append(")\n");
        }

        String response = client.askGemini(batchPrompt.toString());

        System.out.println("\nRAW BATCH RESPONSE\n" + response);

        try {response = response
                .replace("```json", "")
                .replace("```", "")
                .trim();

            JSONObject fullJson = new JSONObject(response);

            String textBlock = fullJson
                    .getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text");

            textBlock = textBlock
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            String jsonOnly = textBlock.substring(textBlock.indexOf("["), textBlock.lastIndexOf("]") + 1);

            JSONArray jsonArray = new JSONArray(jsonOnly);

            for (int i = 0; i < transactions.size() && i < jsonArray.length(); i++) {
                String category = jsonArray.getJSONObject(i)
                        .optString("category", "MISCELLANEOUS")
                        .trim()
                        .toUpperCase()
                        .replaceAll("[^A-Z ]", "");

                category = normalizeCategory(category);

                if (!TransactionPromptBuilder.isValid(category)) {
                    category = "MISCELLANEOUS";
                }

                transactions.get(i).setCategory(category.toUpperCase());
            }

        } catch (Exception e) {
            System.err.println("Failed to parse batch, applying fallback");
            transactions.forEach(t -> t.setCategory("MISCELLANEOUS"));
        }

    }
    private String normalizeCategory(String raw) {

        raw = raw.toLowerCase();

        return switch (raw) {
            case "food", "takeout", "restaurant", "eat out" -> "DINING OUT";
            case "shopping", "clothes", "electronics" -> "SHOPPING";
            case "transport", "taxi", "uber", "bus", "car" -> "TRANSPORTATION";
            case "entertainment", "movies", "concert", "streaming" -> "ENTERTAINMENT";
            case "utilities", "internet", "phone", "electricity" -> "UTILITIES";
            case "groceries" -> "GROCERIES";
            case "gym", "fitness", "run" -> "FITNESS";
            default -> raw;
        };
    }

}
