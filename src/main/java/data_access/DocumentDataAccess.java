package data_access;

import entity.Document;
import entity.User;
import use_case.create_document.CreationDataAccessInterface;
import use_case.save_document.SavingDataAccessInterface;

import java.io.*;
import java.util.*;

/**
 * DAO for document data implemented using a File to persist the data.
 */
public class DocumentDataAccess implements CreationDataAccessInterface, SavingDataAccessInterface {

    private static final String HEADER = "name,createdBy,accessedBy,contents";

    private final File csvFile;
    private final Map<String, Integer> headers = new LinkedHashMap<>();
    private final Map<String, Document> documents = new HashMap<>();

    private UserDataAccess userDataAccess;

    /**
     * Construct this DAO for saving to and reading from a local file.
     * @param csvPath the path of the file to save to
     * @throws RuntimeException if there is an IOException when accessing the file
     */
    public DocumentDataAccess(String csvPath) {
        csvFile = new File(csvPath);

        headers.put("name", 0);
        headers.put("createdBy", 1);
        headers.put("accessedBy", 2);
        headers.put("contents", 3);
    }

    public void setUserDataAccess(UserDataAccess userDataAccess) { this.userDataAccess = userDataAccess; }

    public void load() {
        if (userDataAccess == null) {
            throw new IllegalStateException(
               "UserDataAccess must be set before loading documents."
            );
        }

        if (csvFile.length() == 0) {
            save();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            final String header = reader.readLine();

            if (!header.equals(HEADER)) {
                throw new RuntimeException(String.format("header should be%n: %s%n but was:%n%s", HEADER, header));
            }

            String row;
            while ((row = reader.readLine()) != null) {
                final String[] col = row.split(",", 4);
                final String name = String.valueOf(col[headers.get("name")]);
                final User createdBy = userDataAccess.get(String.valueOf(col[headers.get("createdBy")]));

                final String accessedByRow = String.valueOf(col[headers.get("accessedBy")]);
                final String[] accessedByCol = accessedByRow.split(";");
                final List<User> accessedBy = new ArrayList<>();
                for (String accessedByUsername : accessedByCol) {
                        accessedBy.add(userDataAccess.get(String.valueOf(accessedByUsername)));
                }

                final String contents = String.valueOf(col[headers.get("contents")]);
                final Document document = new Document(name, createdBy, accessedBy, contents);
                documents.put(name, document);

                createdBy.addDocumentCreated(document);
                for (User accessedByUser : accessedBy) {
                    if (accessedByUser != null) {
                        accessedByUser.addDocumentAccessed(document);
                    }
                }
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void save() {
        final BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(csvFile));
            writer.write(String.join(",", headers.keySet()));
            writer.newLine();

            for (Document document : documents.values()) {
                StringJoiner accessedBy = new StringJoiner(";");
                for (User accessedByUser : document.getAccessedBy()) {
                    accessedBy.add(accessedByUser.getName());
                }
                final String line = String.format("%s,%s,%s,%s",
                        document.getName(), document.getCreator().getName(), accessedBy, document.getContents());
                writer.write(line);
                writer.newLine();
            }

            writer.close();

        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void save(Document document) {
        documents.put(document.getName(), document);
        this.save();
    }

    public Document get(String name) { return documents.get(name); }

    @Override
    public boolean existsByName(String name) { return documents.containsKey(name); }
}
