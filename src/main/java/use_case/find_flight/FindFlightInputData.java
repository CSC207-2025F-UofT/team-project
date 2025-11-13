package use_case.find_flight;

import java.time.LocalDate;

public class FindFlightInputData {
    public final String originLocationCode;
    public final String destinationLocationCode;
    public final String departureDate;
    public final int adults;
    public final boolean nonStop;

    public FindFlightInputData(String originLocationCode,
                               String destinationLocationCode,
                               int adults,
                               boolean nonStop) {
        this.originLocationCode = originLocationCode;
        this.destinationLocationCode = destinationLocationCode;
        this.departureDate = LocalDate.now().toString();
        this.adults = adults;
        this.nonStop = nonStop;
    }
}
