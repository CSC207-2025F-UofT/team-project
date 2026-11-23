package use_case.get_data;

import entities.CognitiveData;
import java.util.List;

public interface get_dataDataAccessInterface {
    List<CognitiveData> fetchAll();
    List<CognitiveData> fetchByUsername(String username);
}