package use_case.start_round;

import entity.AnswerCity;
import entity.Round;
import use_case.MapImageProvider;

public class StartRoundInteractor implements StartRoundInputBoundary {
    private final StartRoundDataAccessInterface dataAccess;
    private final StartRoundOutputBoundary presenter;
    private final MapImageProvider imageProvider;

    public StartRoundInteractor(
        StartRoundDataAccessInterface dataAccess,
        StartRoundOutputBoundary presenter,
        MapImageProvider imageProvider
    ) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
        this.imageProvider = imageProvider;
    }

    @Override
    public void execute() {
        AnswerCity answerCity = dataAccess.getRandomAnswerCity();

        Round round = new Round(answerCity);
        dataAccess.saveRound(round);

        System.out.println(answerCity.getName());

        String imageURL = imageProvider.getImageURL(answerCity, round.getZoomStep());
        presenter.prepareSuccessView(new StartRoundOutputData(imageURL, round.getAttemptsLeft()));
    }
}
