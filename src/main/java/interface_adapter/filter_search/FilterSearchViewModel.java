package interface_adapter.filter_search;

public class FilterSearchViewModel {

    private FilterSearchState state = new FilterSearchState();

    public FilterSearchState getState() {
        return state;
    }

    public void setState(FilterSearchState newState) {
        this.state = newState;
    }
}
