package interface_adapter.result;
import use_case.ResultScreen.ResultScreenOutputBoundary;
import use_case.ResultScreen.ResultScreenOutputData;

public class ResultPresenter implements ResultScreenOutputBoundary {
    private final ResultViewModel vm;

    public ResultPresenter(ResultViewModel vm) {
        this.vm = vm;
    }

    @Override
    public void prepareSuccessView(ResultScreenOutputData outputData) {
        vm.setMessage(outputData.getMessage());
    }

}
