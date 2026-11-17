package use_case.search;

public class SearchOutputData {
    private final String result;

    public SearchOutputData(String result) {
        this.result = result;
    }

    public String getResult(){
        return this.result;
    }
}
