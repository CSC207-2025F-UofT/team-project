package entity;
import java.time.LocalDate;

public class Transaction {
    private final LocalDate date;
    private final Source source;
    private final double amount;


    //create transaction class with category
    public Transaction(Source source, double amount, LocalDate date){
        this.date = date;
        this.source = source;
        this.amount =  amount;

    }



    public double getAmount(){
        return this.amount;
    }


    public Source getSource(){
        return this.source;
    }

    public LocalDate getDate(){
        return this.date;
    }


}
