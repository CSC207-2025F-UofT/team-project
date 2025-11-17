package interface_adaptor.Menu;

import star_rate.StarRateInputBoundary;
import star_rate.StarRateInputData;

public class StarRateController {
    private StarRateInputBoundary inputBoundary;
    public StarRateController(StarRateInputBoundary input){
        this.inputBoundary = input;
    }
    public void execute(int rating, String id){
        StarRateInputData inputData = new StarRateInputData(rating, id);
        inputBoundary.execute(inputData);
    }
}
