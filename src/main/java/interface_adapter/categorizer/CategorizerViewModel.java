package interface_adapter.categorizer;

import entity.Transaction;
import interface_adapter.ViewModel;

import java.util.List;

public class CategorizerViewModel extends ViewModel<CategorizerState> {

    private CategorizerState state;

    public CategorizerViewModel() {
        super("categorizer");
        this.state = new CategorizerState();
    }

    public void setTransactions(List<Transaction> t) {
        state.setTransactions(t);
    }

    public List<Transaction> getTransactions() {
        return state.getTransactions();
    }
}
