package use_case.clicking;
import entity.Media;
import entity.MediaDetailsResponse;

public class ClickingInteractor implements ClickingInputBoundary{
    private final ClickingOutputBoundary presenter;
    private ClickingDataAccessInterface repository;
    public ClickingInteractor(ClickingOutputBoundary presenter,
                              ClickingDataAccessInterface repository) {
        this.presenter = presenter;
        this.repository = repository;
    }
    public void execute(ClickingInputData inputData) {
        MediaDetailsResponse media = repository.fetchDetailsById(inputData.getMediaId());
        if (media == null) {
            presenter.prepareFailureView("Media not found.");
            return;
        }

        ClickingOutputData outputData = new ClickingOutputData(
                media.getTitle(),
                media.getYear(),
                media.getLanguage(),
                media.getRating(),
                String.join(", ", media.getGenres()),
                media.getOverview(),
                media.getPosterUrl()
        );
        presenter.prepareSuccessView(outputData);

    }
}
