package app;

import data_access.GeminiFlashcardGenerator;
import data_access.HardCodedCourseLookup;
import entities.Course;
import entities.PDFFile;
import interface_adapters.flashcards.GenerateFlashcardsPresenter;
import usecases.GenerateFlashcardsInputBoundary;
import usecases.GenerateFlashcardsInteractor;

import java.io.File;
import java.net.URL;

public class AppBuilder {

    public void runFlashcardGeneration() {
        try {
            HardCodedCourseLookup lookup = new HardCodedCourseLookup();
            Course course = lookup.getCourseById("RLG200");

            if (course == null || course.getUploadedFiles().isEmpty()) {
                throw new IllegalStateException("No course or PDF found.");
            }

            PDFFile pdf = course.getUploadedFiles().get(0);

            // Load PDF from src/main/resources/
            ClassLoader cl = getClass().getClassLoader();
            URL fileUrl = cl.getResource(pdf.getPath().toString());

            if (fileUrl == null) {
                System.out.println("ERROR: PDF not found in resources folder.");
                return;
            }

            File file = new File(fileUrl.toURI());

            // Pass absolute path to Gemini generator
            String content = file.getAbsolutePath();

            GeminiFlashcardGenerator generator = new GeminiFlashcardGenerator();
            GenerateFlashcardsPresenter presenter = new GenerateFlashcardsPresenter();
            GenerateFlashcardsInputBoundary interactor =
                    new GenerateFlashcardsInteractor(generator, presenter);

            System.out.println("Generating flashcards from PDF...");
            interactor.execute(course.getCourseId(), content);

        } catch (Exception e) {
            System.err.println("Error running flashcard demo: " + e.getMessage());
        }
    }
}