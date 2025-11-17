package data_access;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentConfig;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import com.google.gson.Gson;
import entities.Flashcard;
import entities.FlashcardSet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * GeminiFlashcardGenerator: supports topic string or PDF file.
 */
public class GeminiFlashcardGenerator implements FlashcardGenerator {

    private final String apiKey;
    private final Client client;
    private final GenerateContentConfig generationConfig;
    private final Gson gson;
    private final String flashcardPrompt;

    public GeminiFlashcardGenerator() {
        apiKey = System.getenv("GEMINI_API_KEY");
        client = Client.builder().apiKey(apiKey).build();

        ImmutableMap<String, Object> flashcardSchema = ImmutableMap.of(
                "type", "object",
                "properties", ImmutableMap.of(
                        "questions", ImmutableMap.of("type", "array", "items", ImmutableMap.of("type", "string")),
                        "answers", ImmutableMap.of("type", "array", "items", ImmutableMap.of("type", "string"))
                ),
                "required", ImmutableList.of("questions", "answers")
        );

        generationConfig = GenerateContentConfig.builder()
                .responseMimeType("application/json")
                .candidateCount(1)
                .responseJsonSchema(flashcardSchema)
                .build();

        flashcardPrompt = """
                You are an AI tutor. Based on the provided content, generate 5 short flashcards.
                Return JSON:
                {
                    "questions": [],
                    "answers": []
                }
                """;

        gson = new Gson();
    }

    @Override
    public FlashcardSet generateForCourse(String courseName, String contentInput) throws IOException {

        List<Part> partsList = new ArrayList<>();
        partsList.add(Part.fromText(flashcardPrompt));

        // Check whether contentInput is a PDF filename
        File maybePdf = new File(contentInput);
        if (maybePdf.exists() && contentInput.toLowerCase().endsWith(".pdf")) {
            byte[] pdfBytes = Files.readAllBytes(maybePdf.toPath());

            partsList.add(
                    Part.fromBytes(pdfBytes, "application/pdf")
            );

        } else {
            partsList.add(Part.fromText(contentInput));
        }

        Content request = Content.fromParts(partsList.toArray(new Part[0]));

        GenerateContentResponse response =
                client.models.generateContent("gemini-2.5-flash", request, generationConfig);

        FlashcardResponse parsed = gson.fromJson(response.text(), FlashcardResponse.class);
        if (parsed == null || parsed.questions == null || parsed.answers == null) {
            throw new IOException("Invalid response from Gemini.");
        }

        List<Flashcard> flashcards = new ArrayList<>();
        int size = Math.min(parsed.questions.size(), parsed.answers.size());
        for (int i = 0; i < size; i++) {
            flashcards.add(new Flashcard(parsed.questions.get(i), parsed.answers.get(i)));
        }

        return new FlashcardSet(courseName, flashcards);
    }

    private static class FlashcardResponse {
        List<String> questions;
        List<String> answers;
    }
}