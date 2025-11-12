package use_case.find_flight;

/**
 * The Input Data for the Find Flight use case
 */

public class FindFlightInputData {

    private final String from;
    private final String to;
    private final int day;
    private final String month;
    private final int year;

    public FindFlightInputData(String from, String to, String day, String month, String year) {
        this.from = from;
        this.to = to;
        this.day = Integer.parseInt(day);
        this.month = month;
        this.year = Integer.parseInt(year);
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
