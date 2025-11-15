package use_case.find_flight;

import data_access.InMemoryFlightDataAccessObject;
import helpers.SearchInfoVerifier;
import helpers.CityCodeConverter;
import entity.FlightSearchInformation;
import entity.Flight;

import java.util.List;

public class FindFlightInteractor implements FindFlightInputBoundary{

    private final SearchInfoVerifier searchInfoVerifier;
    private final FindFlightOutputBoundary flightPresenter;
    private final LogSearchInfoDataAccessInterface logSearchInfoDataObject;
    private final CityCodeConverter cityCodeConverter;


    public FindFlightInteractor(SearchInfoVerifier searchInfoVerifier, FindFlightOutputBoundary findFlightOutputBoundary,
                                LogSearchInfoDataAccessInterface logSearchInfoDataAccessInterface,  CityCodeConverter cityCodeConverter) {
        this.searchInfoVerifier = searchInfoVerifier;
        this.flightPresenter = findFlightOutputBoundary;
        this.logSearchInfoDataObject = logSearchInfoDataAccessInterface;
        this.cityCodeConverter = cityCodeConverter;
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

            int day = findFlightInputData.getDay();
            String month = findFlightInputData.getMonth();
            int year = findFlightInputData.getYear();
            String departureData = String.format("%04d-%02d-%02d", day, month, year);

            String originCityName = findFlightInputData.getFrom();
            String destCityName = findFlightInputData.getTo();

            String originLocationCode = cityCodeConverter.getCode(originCityName);
            String destinationLocationCode = cityCodeConverter.getCode(destCityName);
            int adults = 1;
            boolean nonstop = true;

            List<Flight> flights = InMemoryFlightDataAccessObject.search(
                    originLocationCode, destinationLocationCode,
                    departureData, adults, nonstop
            );
//              Wait for finishing the Presenter part
//            FindFlightOutputData outputData = new FindFlightOutputData(flights, flightSearchInformation);
//            flightPresenter.prepareSuccessView(outputData);
        }
    }

}
