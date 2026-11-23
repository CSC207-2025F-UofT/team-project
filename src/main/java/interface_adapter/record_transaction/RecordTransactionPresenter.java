package interface_adapter.record_transaction;

import use_case.record_transaction.RecordTransactionOutputBoundary;

/**
 * The Presenter for the Record Transaction Use Case.
 */
public class RecordTransactionPresenter
        implements RecordTransactionOutputBoundary {

    private final RecordTransactionViewModel recordTransactionViewModel;

    public RecordTransactionPresenter(
            RecordTransactionViewModel recordTransactionViewModel) {
        this.recordTransactionViewModel = recordTransactionViewModel;
    }

    @Override
    public void prepareSuccessView() {
        final RecordTransactionState state =
                recordTransactionViewModel.getState();

        state.setAmount("");
        state.setError(null);
        state.setSuccessMessage("Transaction recorded successfully.");

        recordTransactionViewModel.notifyStateChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final RecordTransactionState state =
                recordTransactionViewModel.getState();

        state.setError(error);
        state.setSuccessMessage(null);

        recordTransactionViewModel.notifyStateChanged();
    }
}