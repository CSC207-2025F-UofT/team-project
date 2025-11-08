package entity;

public class Transaction {
    private final int date;
    private final String  source;
    private final double amount;
    private String category;


    //create transaction class with category
    public Transaction(String date, String source, double amount){
        this.date = get_date(date);
        this.source = source;
        this.amount =  amount;
        this.category =  get_category(source);


    }

    public String get_category(String source){

        //TODO: fetching the category from the database

        return "category";

    }

    public int get_date(String date){

        //TODO: getting the month and date as a integer- 11/10/2024 = 11102024

        //Assumes dates is in MM/DD/YYYY format
        String clean_date = "";
        for (int i = 0; i< date.length(); i++){
            if (date.charAt(i) != '/') {
                clean_date += date.charAt(i);
            }
        }

        return Integer.parseInt(clean_date);

    }

}
