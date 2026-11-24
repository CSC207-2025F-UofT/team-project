package data_access;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import entity.SideQuest;
import entity.Location;

import java.io.*;
import java.nio.file.*;
import java.util.*;

class FileSideQuestDataAccessObject {
    /* This class has two main functions: Storing the SideQuest entity as an entry in the side-quests.csv file, as
    well as creating a SideQuest entity based on a specific entry (accessed by the id of the SideQuest). */

    private final Path csvFile;

    public FileSideQuestDataAccessObject(String filePath) {
        this.csvFile = Path.of(filePath);
        initializeFile();
    }

    private void initializeFile() {
        // TODO: Create file if it does not exist
    }

    public void save(SideQuest sideQuest) {
        // TODO: Use CSVWriter
    }

    public List<SideQuest> getAllQuests() {
        // TODO: Use CSVReader
        return new ArrayList<>();
    }

    public List<String> getAllTags() {
        // TODO
        return new ArrayList<>();
    }

    private String[] serializeQuest(SideQuest sideQuest) {
        // TODO: Convert entity attributes into String[]
        return null;
    }

    private SideQuest deserializeQuest(String[] row) {
        // TODO: Reconstruct entity from String[]
        return null;
    }
}