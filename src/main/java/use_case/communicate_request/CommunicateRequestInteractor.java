package use_case.communicate_request;

import entity.CommunicationBoard;
import entity.CommunicationDraft;
import entity.Pictogram;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Implements UC1 - Build a Request.
 */
public class CommunicateRequestInteractor
        implements CommunicateRequestInputBoundary {

    static final String PICTOGRAM_NOT_FOUND =
            "That pictogram is not available on the board.";

    private final CommunicateRequestDataAccessInterface dataAccess;
    private final CommunicateRequestOutputBoundary presenter;
    private final CommunicationDraft draft;

    public CommunicateRequestInteractor(
            CommunicateRequestDataAccessInterface dataAccess,
            CommunicateRequestOutputBoundary presenter,
            CommunicationDraft draft) {

        this.dataAccess =
                Objects.requireNonNull(dataAccess, "dataAccess cannot be null");

        this.presenter =
                Objects.requireNonNull(presenter, "presenter cannot be null");

        this.draft =
                Objects.requireNonNull(draft, "draft cannot be null");
    }

    @Override
    public void loadBoard() {
        presentCurrentState();
    }

    @Override
    public void selectPictogram(int pictogramId) {
        CommunicationBoard board = dataAccess.getBoard();
        Pictogram pictogram = findPictogram(board, pictogramId);

        if (pictogram == null) {
            presenter.prepareFailView(PICTOGRAM_NOT_FOUND);
            return;
        }

        draft.addPictogram(pictogram);
        presentCurrentState();
    }

    @Override
    public void removeLastSelection() {
        if (!draft.isEmpty()) {
            draft.removeLastPictogram();
        }

        presentCurrentState();
    }

    @Override
    public void clearDraft() {
        draft.clear();
        presentCurrentState();
    }

    private Pictogram findPictogram(
            CommunicationBoard board,
            int pictogramId) {

        for (Pictogram pictogram : board.getPictograms()) {
            if (pictogram.getId() == pictogramId) {
                return pictogram;
            }
        }

        return null;
    }

    private void presentCurrentState() {
        CommunicationBoard board = dataAccess.getBoard();

        List<CommunicateRequestOutputData.PictogramItem> items =
                new ArrayList<>();

        for (Pictogram pictogram : board.getPictograms()) {
            items.add(
                    new CommunicateRequestOutputData.PictogramItem(
                            pictogram.getId(),
                            pictogram.getLabel(),
                            pictogram.getImageLocation()));
        }

        presenter.prepareSuccessView(
                new CommunicateRequestOutputData(
                        items,
                        draft.previewText(),
                        !draft.isEmpty()));
    }
}
