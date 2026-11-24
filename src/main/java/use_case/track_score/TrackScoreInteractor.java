package use_case.track_score;

public class TrackScoreInteractor implements TrackScoreInputBoundary{

        private final TrackScoreDataAccessInterface dataAccess;
        private final TrackScoreOutputBoundary presenter;

        public TrackScoreInteractor(TrackScoreDataAccessInterface dataAccess,
                                    TrackScoreOutputBoundary presenter) {
            this.dataAccess = dataAccess;
            this.presenter = presenter;
        }

        @Override
        public void execute(TrackScoreInputData inputData) {
            //TODO body

        }
    }

