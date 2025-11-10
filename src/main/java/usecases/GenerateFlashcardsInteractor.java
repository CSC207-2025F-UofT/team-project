package usecases;

import data_access.FlashcardGenerator;
import entities.FlashcardSet;

public class GenerateFlashcardsInteractor implements GenerateFlashcardsInputBoundary {
    private final FlashcardGenerator generator;
    private final GenerateFlashcardsOutputBoundary presenter;

    public GenerateFlashcardsInteractor(FlashcardGenerator generator, GenerateFlashcardsOutputBoundary presenter) {
        this.generator = generator;
        this.presenter = presenter;
    }

    @Override
    public void execute(GenerateFlashcardsRequestModel requestModel) {
        FlashcardSet set = generator.generateForCourse(
                requestModel.getCourseName(),
                requestModel.getContent()
        );

        GenerateFlashcardsResponseModel response = new GenerateFlashcardsResponseModel(set);
        presenter.presentFlashcards(response);
    }
}