package usecase.castspell;

import yuhangusecase.castspell.CastOutputBoundary;
import yuhangusecase.castspell.CastOutputData;

public class RecordingCastOutput implements CastOutputBoundary {
    private CastOutputData outputData;
    private String errorMessage;

    @Override
    public void prepareSuccessView(CastOutputData outputData) {
        this.outputData = outputData;
        this.errorMessage = null;
    }

    @Override
    public void prepareFailView(String errorMessage) {
        this.errorMessage = errorMessage;
        this.outputData = null;
    }

    public CastOutputData getOutputData() {
        return outputData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
