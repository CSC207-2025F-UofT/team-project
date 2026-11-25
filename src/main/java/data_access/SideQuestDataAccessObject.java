package data_access;

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;

import entity.SideQuest;

import java.io.*;
import java.nio.file.*;
import java.util.*;

class SideQuestDataAccessObject {
    /* This class has two main functions: Storing the SideQuest entity as an entry in the side-quests.csv file, as
    well as creating a SideQuest entity based on a specific entry (accessed by the id of the SideQuest). */

    private final Path csvFile;

    public SideQuestDataAccessObject(String filePath) {
        this.csvFile = Path.of(filePath);
    }

    public void save(SideQuest sideQuest) {
//        CSVWriter writer = new CSVWriterBuilder(new FileWriter(csvFile))
//                .withSeparator('\t')
//                .build();
//        // feed in your array (or convert your data to an array)
//        ArrayList<String> unformattedInterests = sideQuest.getInterests();
//        String formattedInterests = '(' + String.join(',', unformattedInterests) + ')';
//
//
//        String[] entries = "first#second#third".split("#");
//        writer.writeNext(entries);
//        writer.close();

    }

    public List<SideQuest> getAllQuests() {

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