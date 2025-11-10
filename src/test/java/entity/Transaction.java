package entity;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Transaction {
    private final LocalDate date;
    private final String  source;
    private final double amount;
    private String category;


    //create transaction class with category
    public Transaction(String source, double amount, LocalDate date, String category){
        this.date = date;
        this.source = source;
        this.amount =  amount;
        this.category = source;


    }



    public double getAmount(){
        return this.amount;
    }

    public String getCategory(){
        return this.category;
    }

    public String getSource(){
        return this.source;
    }

    public LocalDate getDate(){
        return this.date;
    }


}
