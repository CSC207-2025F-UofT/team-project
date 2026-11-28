package interface_adapter.watchlist;

import use_case.watchlist.WatchListOutputdata;
import use_case.watchlist.WatchlistOutputBoundary;

import java.util.List;

public class WatchlistPresenter implements WatchlistOutputBoundary {

    private final WatchListViewModel viewModel;

    public WatchlistPresenter(WatchListViewModel viewModel){
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(WatchListOutputdata outputdata){
        viewModel.setStockWatchers(outputdata.getStockWatchers());
        viewModel.setErrorMessage(null);

        if(!outputdata.getChanges().isEmpty()){
            viewModel.setChangesText(formatChanges(outputdata));
        }
        else{
            viewModel.setChangesText(retainLines(viewModel.getChangesText(), outputdata.getStockWatchers()));
        }

        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage){
        viewModel.setErrorMessage(errorMessage);
        viewModel.firePropertyChanged();
    }

    private String formatChanges(WatchListOutputdata outputData){
        StringBuilder message = new StringBuilder();

        for(WatchListOutputdata.Change change : outputData.getChanges()){
            if(change.hasError()){
                message.append(change.getStockWatcher()).append(": ").append(change.getErrorMessage());
            }
            else{
                message.append(String.format("%s: %.2f (%+.2f%%)",change.getStockWatcher(),
                        change.getLatestPrice(),change.getPercentChange()));
            }
            message.append("\n");
        }
        return message.toString();
    }

    private String retainLines(String text, List<String> stockWatchers){
        StringBuilder retained = new StringBuilder();
        for(String line : text.split("\n")){
            String stockWatcher = line.split(":")[0];
            if(stockWatchers.contains(stockWatcher)){
                retained.append(line).append("\n");
            }
        }
        return retained.toString();
    }
}
