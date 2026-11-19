package use_case.playcard;

public class PlayCardInteractor implements PlayCardInputBoundary{

    private final PlayCardDataAccessInterface dataAccessObject;
    private final PlayCardOutputBoundary playCardPresenter;

    public PlayCardInteractor(PlayCardDataAccessInterface dataAccessInterface,
                              PlayCardOutputBoundary playCardOutputBoundary) {
        this.dataAccessObject = dataAccessInterface;
        this.playCardPresenter = playCardOutputBoundary;
    }


    @Override
    public void execute(PlayCardInputData playCardInputData) {

    }
}
