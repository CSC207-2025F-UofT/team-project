package data_access;

import com.google.genai.Client;
import com.google.genai.types.Content;
import com.google.genai.types.GenerateContentResponse;
import com.google.genai.types.Part;
import entities.Course;
import entities.LectureNotes;
import entities.PDFFile;
import usecases.lecturenotes.NotesGeminiGateway;

import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class NotesGeminiApiDataAccess implements NotesGeminiGateway {
    private final Client client = new Client();              // reads GEMINI_API_KEY
    private final String model = "gemini-2.5-flash";

    @Override
    public LectureNotes generateNotes(Course course, String topic) throws Exception {
        List<Part> parts = new ArrayList<>();
        parts.add(Part.fromText(buildPrompt(course, topic)));
        for (PDFFile f : course.getUploadedFiles()) {
            byte[] bytes = Files.readAllBytes(f.getPath());
            parts.add(Part.fromBytes(bytes, "application/pdf"));
        }
        Content content = Content.fromParts(parts.toArray(new Part[0]));
        GenerateContentResponse resp = client.models.generateContent(model, content, null);
        String text = resp.text();
        return new LectureNotes(course.getCourseId(), topic, text, LocalDateTime.now());
    }

    private String buildPrompt(Course course, String topic) {
        return "Create concise, well-structured lecture notes for course "
                + course.getCourseId()
                + " on topic: " + topic + ". Use headings and bullet points. Use only the attached PDFs.";
    }
}