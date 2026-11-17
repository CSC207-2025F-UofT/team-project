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
    public long uniqueId() {
        Random random = new Random();
        Set<Long> listOfIds = readData();
        long randId;
        do {
            randId = Math.abs(random.nextLong());
        } while (listOfIds.contains(randId));

        return randId;
    }

    private Set<Long> readData() {
        File idFiles = new File("projectIds.txt");
        Set<Long> listOfLong = new HashSet<Long>();
        try (Scanner reader = new Scanner(idFiles)) {
            while (reader.hasNextLine()) {
                String data = reader.nextLine();
                listOfLong.add(Long.valueOf(data));
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File Not Found");
            e.printStackTrace();
        }
        return listOfLong;
    }

    @Override
    public long getId() {
        return uniqueId();
    }

    @Override
    public void writeIdToData(long id) {
        Path filePath = Path.of("projectIds.txt");
        String newData = id + System.lineSeparator();

        try {
            Files.writeString(filePath, newData, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("Data appended successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}