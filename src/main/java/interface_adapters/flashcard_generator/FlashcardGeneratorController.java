package interface_adapters.flashcard_generator;

import java.util.List;

import use_case.flashcard_generator.FlashcardGeneratorInputBoundary;
import use_case.flashcard_generator.FlashcardGeneratorInputData;
import use_case.flashcard_generator.FlashcardGeneratorInputData.TextPiece;

/**
 * Controller for FlashcardGenerator. Passes InputData to the Interactor through InputBoundary.
 */
public class FlashcardGeneratorController {
    private final FlashcardGeneratorInputBoundary interactor;

    public FlashcardGeneratorController(FlashcardGeneratorInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Creates InputData object and passes it to Interactor
     */
    public void execute(List<TextPiece> textPieceList) {
        FlashcardGeneratorInputData data = new FlashcardGeneratorInputData(textPieceList);
            interactor.execute(data);

    }
}
