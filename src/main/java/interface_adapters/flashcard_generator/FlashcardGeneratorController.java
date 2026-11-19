package interface_adapters.flashcard_generator;

import java.util.List;

import use_case.flashcard_generator.FlashcardGeneratorInputBoundary;
import use_case.flashcard_generator.FlashcardGeneratorInputData;
import use_case.flashcard_generator.FlashcardGeneratorInputData.TextPiece;


public class FlashcardGeneratorController {
    private final FlashcardGeneratorInputBoundary interactor;

    public FlashcardGeneratorController(FlashcardGeneratorInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute(List<TextPiece> textPieceList) {
        FlashcardGeneratorInputData data = new FlashcardGeneratorInputData(textPieceList);
            interactor.execute(data);

    }
}
