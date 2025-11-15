package stock.usecase_stocksearch;

public class StockSearchInputData {
    private final String keywords;


    public StockSearchInputData(String keywords) {
        this.keywords = keywords;
    }
    
    public String getKeywords() {
        return keywords;
    }
}