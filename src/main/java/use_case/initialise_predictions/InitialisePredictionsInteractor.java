package use_case.initialise_predictions;
import entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

public class InitialisePredictionsInteractor implements InitialisePredictionsInputBoundary{
    private final BootstrapDataAccessInterface bootstrapDataAccess;
    private final GameWeekDataAccessInterface gameWeekDataAccess;
    private final ModelCoefficientDataAccessInterface modelCoefficientAccess;
    private final InitialisePredictionsOutputBoundary presenter;

    public InitialisePredictionsInteractor(
            BootstrapDataAccessInterface bootstrapDataAccess,
            GameWeekDataAccessInterface gameWeekDataAccess,
            ModelCoefficientDataAccessInterface modelCoefficientAccess,
            InitialisePredictionsOutputBoundary presenter) {
        this.bootstrapDataAccess = bootstrapDataAccess;
        this.gameWeekDataAccess = gameWeekDataAccess;
        this.modelCoefficientAccess = modelCoefficientAccess;
        this.presenter = presenter;
    }
    @Override
    public void execute(InitialisePredictionsInputData inputData) {


    }
}
