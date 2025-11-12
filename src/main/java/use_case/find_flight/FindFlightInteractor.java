package use_case.find_flight;

import helpers.SearchInfoVerifier;
import entity.FlightSearchInformation;

public class FindFlightInteractor implements FindFlightInputBoundary{

    private final SearchInfoVerifier searchInfoVerifier;
    private final FindFlightOutputBoundary flightPresenter;
    private final LogSearchInfoDataAccessInterface logSearchInfoDataObject;


    public FindFlightInteractor(SearchInfoVerifier searchInfoVerifier, FindFlightOutputBoundary findFlightOutputBoundary,  LogSearchInfoDataAccessInterface logSearchInfoDataAccessInterface) {
        this.searchInfoVerifier = searchInfoVerifier;
        this.flightPresenter = findFlightOutputBoundary;
        this.logSearchInfoDataObject = logSearchInfoDataAccessInterface;
    }

    @Override
    public void execute(FindFlightInputData findFlightInputData) {
        if (!searchInfoVerifier.isCityValid(findFlightInputData.getFrom())) {
            flightPresenter.prepareFailView("Please enter a valid city to depart from");
        }

        else if (!searchInfoVerifier.isCityValid(findFlightInputData.getTo())) {
            flightPresenter.prepareFailView("Please enter a valid city to travel to");
        }

        else if (!searchInfoVerifier.isDayValid(findFlightInputData.getDay(), findFlightInputData.getMonth(),  findFlightInputData.getYear())) {
            flightPresenter.prepareFailView("The day you entered is not valid");
        }

        else if (!searchInfoVerifier.isMonthValid(findFlightInputData.getMonth(), findFlightInputData.getYear())) {
            flightPresenter.prepareFailView("The month you entered is not valid");
        }

        else {
            // The search info the user entered passed all the checks so now it is safe to call the APi
            // and log the data. After the API is called and its data is returned the flights need to be converted to
            // Flight entities and these entities need to be stored in memory. The presenter also needs to prepare the
            // "Success view" which takes the user to a new UI with all the flights.


            // This is the call to my logger
            final FlightSearchInformation flightSearchInformation = new FlightSearchInformation(findFlightInputData.getFrom(), findFlightInputData.getTo(),
                                                                    findFlightInputData.getDay(), findFlightInputData.getMonth(), findFlightInputData.getYear());
            logSearchInfoDataObject.log(flightSearchInformation);

        }
    }

}
