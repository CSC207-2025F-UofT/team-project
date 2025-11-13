package entity;

/**
 * Entity representing the Flight Search Information
 */

public class FlightSearchInformation {

    private final String from;
    private final String to;
    private final int day;
    private final String month;
    private final int year;

    public FlightSearchInformation(String from, String to, int day, String month, int year) {
        this.from = from;
        this.to = to;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
}
