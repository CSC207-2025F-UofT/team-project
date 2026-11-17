package dataBase;

import useCases.ProjectDataAccessInterface;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.nio.file.Path;

public class ProjectIdAccess implements ProjectDataAccessInterface{
    private final long id;
    private final Set<Long> listOfIds;

    public ProjectIdAccess() {
        listOfIds = new HashSet<Long>();
        id = uniqueId(listOfIds);
    }

    public long uniqueId(Set<Long> listOfIds) {
        Random random = new Random();
        return Math.abs(random.nextLong());
    }

    private void readData() {
        File idFiles = new File("projectIds.txt");
        try (Scanner reader = new Scanner(idFiles)) {
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                listOfIds.add(Long.valueOf(data));
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            e.printStackTrace();
        }
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void writeIdToData(long id) {
        Path filePath = Path.of("projectIds.txt");
        String newData = Long.toString(id);

        try {
            Files.writeString(filePath, newData, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("Data appended successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}