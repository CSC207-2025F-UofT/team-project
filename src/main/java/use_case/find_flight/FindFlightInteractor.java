package use_case.find_flight;

// Make sure FindFlightUserDataAccessInterface is imported
import use_case.find_flight.FindFlightUserDataAccessInterface;
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

    // 1. ADD THIS NEW FIELD
    private final FindFlightUserDataAccessInterface flightDataAccessObject;


    // 2. UPDATE THE CONSTRUCTOR TO ACCEPT THE DAO
    public FindFlightInteractor(SearchInfoVerifier searchInfoVerifier, FindFlightOutputBoundary findFlightOutputBoundary,
                                LogSearchInfoDataAccessInterface logSearchInfoDataAccessInterface,
                                FindFlightUserDataAccessInterface flightDataAccessObject, // <-- ADD THIS
                                CityCodeConverter cityCodeConverter) {
        this.searchInfoVerifier = searchInfoVerifier;
        this.flightPresenter = findFlightOutputBoundary;
        this.logSearchInfoDataObject = logSearchInfoDataAccessInterface;
        this.flightDataAccessObject = flightDataAccessObject; // <-- ADD THIS
        this.cityCodeConverter = cityCodeConverter;
    }

    // In helpers/SearchInfoVerifier.java


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

            // --- START OF THE FIX ---

            // 1. GET THE INPUT DATA
            int day = findFlightInputData.getDay();
            int year = findFlightInputData.getYear();
            String originCityName = findFlightInputData.getFrom();
            String destCityName = findFlightInputData.getTo();

            // 2. FIX THE DATE FORMATTING BUG
            // You must get the integer value for the month
            int monthAsInt = searchInfoVerifier.getMonthAsInt(findFlightInputData.getMonth());
            String departureData = String.format("%04d-%02d-%02d", year, monthAsInt, day); // Use Y-M-D order

            // 3. DEFINE ALL YOUR VARIABLES
            String originLocationCode = cityCodeConverter.getCode(originCityName);
            String destinationLocationCode = cityCodeConverter.getCode(destCityName);
            int adults = 1;
            boolean nonstop = true;

            // 4. THE CORRECTED API CALL
            List<Flight> flights = this.flightDataAccessObject.search(
                    originLocationCode, destinationLocationCode,
                    departureData, adults, nonstop
            );

            // --- END OF THE FIX ---

//              Wait for finishing the Presenter part
//            FindFlightOutputData outputData = new FindFlightOutputData(flights, flightSearchInformation);
//            flightPresenter.prepareSuccessView(outputData);
        }

//              Wait for finishing the Presenter part
//            FindFlightOutputData outputData = new FindFlightOutputData(flights, flightSearchInformation);
//            flightPresenter.prepareSuccessView(outputData);
        }
    }
