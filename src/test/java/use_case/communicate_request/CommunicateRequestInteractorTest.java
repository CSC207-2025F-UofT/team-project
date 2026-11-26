package use_case.communicate_request;

import entity.CommunicationBoard;
import entity.CommunicationDraft;
import entity.Pictogram;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommunicateRequestInteractorTest {

    private Pictogram water;
    private Pictogram apple;
    private CommunicationBoard board;
    private CommunicationDraft draft;
    private RecordingPresenter presenter;
    private CommunicateRequestInteractor interactor;

    @BeforeEach
    void setUp() {
        water = new Pictogram(1, "water", "water.png");
        apple = new Pictogram(2, "apple", "apple.png");

        board = new CommunicationBoard();
        board.addPictogram(water);
        board.addPictogram(apple);

        draft = new CommunicationDraft("I want");
        presenter = new RecordingPresenter();

        CommunicateRequestDataAccessInterface dataAccess =
                () -> board;

        interactor =
                new CommunicateRequestInteractor(
                        dataAccess,
                        presenter,
                        draft);
    }

    @Test
    void loadBoardDisplaysAvailablePictograms() {
        interactor.loadBoard();

        assertEquals(
                List.of("water", "apple"),
                labels(presenter.output));

        assertEquals("I want", presenter.output.getPreviewText());
        assertFalse(presenter.output.hasSelection());
    }

    @Test
    void selectPictogramAddsToDraft() {
        interactor.selectPictogram(water.getId());

        assertEquals("I want water.",
                presenter.output.getPreviewText());

        assertTrue(presenter.output.hasSelection());
        assertEquals(List.of(water),
                draft.getSelectedPictograms());
    }

    @Test
    void repeatedSelectionCreatesSeparateOccurrence() {
        interactor.selectPictogram(water.getId());
        interactor.selectPictogram(water.getId());

        assertEquals(
                "I want water water.",
                presenter.output.getPreviewText());

        assertEquals(
                List.of(water, water),
                draft.getSelectedPictograms());
    }

    @Test
    void removeLastSelectionUpdatesPreview() {
        interactor.selectPictogram(water.getId());
        interactor.selectPictogram(apple.getId());

        interactor.removeLastSelection();

        assertEquals(
                "I want water.",
                presenter.output.getPreviewText());

        assertEquals(
                List.of(water),
                draft.getSelectedPictograms());
    }

    @Test
    void removeFromEmptyDraftIsSafe() {
        interactor.removeLastSelection();

        assertEquals(
                "I want",
                presenter.output.getPreviewText());

        assertFalse(presenter.output.hasSelection());
    }

    @Test
    void clearDraftRemovesAllSelections() {
        interactor.selectPictogram(water.getId());
        interactor.selectPictogram(apple.getId());

        interactor.clearDraft();

        assertTrue(draft.isEmpty());
        assertEquals(
                "I want",
                presenter.output.getPreviewText());

        assertFalse(presenter.output.hasSelection());
    }

    @Test
    void unavailablePictogramFailsWithoutChangingDraft() {
        interactor.selectPictogram(999);

        assertNull(presenter.output);
        assertEquals(
                CommunicateRequestInteractor.PICTOGRAM_NOT_FOUND,
                presenter.error);

        assertTrue(draft.isEmpty());
    }

    private static List<String> labels(
            CommunicateRequestOutputData outputData) {

        return outputData.getAvailablePictograms()
                .stream()
                .map(
                        CommunicateRequestOutputData
                                .PictogramItem::getLabel)
                .collect(java.util.stream.Collectors.toList());
    }

    private static class RecordingPresenter
            implements CommunicateRequestOutputBoundary {

        private CommunicateRequestOutputData output;
        private String error;

        @Override
        public void prepareSuccessView(
                CommunicateRequestOutputData outputData) {

            output = outputData;
            error = null;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            output = null;
            error = errorMessage;
        }
    }
}
