package use_case.get_data;

import java.util.List;

import entities.CognitiveData;

public class get_dataOutputData {
    private final List<CognitiveData> entries;
    public get_dataOutputData(List<CognitiveData> entries) { this.entries = entries;}

    public List<CognitiveData> getEntries() {
        return (List<CognitiveData>) entries;
    }
}
