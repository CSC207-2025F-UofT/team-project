package interface_adapter.logged_in;

import use_case.find_flight.FindFlightInputBoundary;
import use_case.find_flight.FindFlightInputData;

/**
 * Controller for Find Flight use case
 */

public class FindFlightController {
     private final FindFlightInputBoundary findFlightUseCaseInteractor;

     public FindFlightController(FindFlightInputBoundary findFlightUseCaseInteractor) {
          this.findFlightUseCaseInteractor = findFlightUseCaseInteractor;
     }

     public void execute (String from, String to, String day, String month, String year) {
        final FindFlightInputData findFlightInputData = new FindFlightInputData(from, to, day, month, year);
        findFlightUseCaseInteractor.execute(findFlightInputData);
     }

}
