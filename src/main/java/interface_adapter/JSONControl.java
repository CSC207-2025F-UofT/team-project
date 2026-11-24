package interface_adapter;

import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/** Original team JSON helper with a consistent read/write path. */
abstract class JSONControl {
    private final String fileName;
    private final Path filePath;
    private JSONObject contents;

    public JSONControl(String file) throws IOException {
        this.fileName = file;
        this.filePath = Path.of("src/main/java/data", file);
        try {
            this.contents = new JSONObject(Files.readString(filePath));
        } catch (IOException e) {
            System.out.println("File not accessible. Has " + file + " been removed or misnamed?");
            throw e;
        }
    }

    public String getFileName() { return this.fileName; }
    public JSONObject getContents() { return this.contents; }

    public void writeContents() throws IOException {
        try {
            Files.createDirectories(filePath.getParent());
            Files.writeString(filePath, this.contents.toString(4));
        } catch (IOException e) {
            System.out.println("Unable to write to file. Has " + this.fileName + " been removed or misnamed?");
            throw e;
        }
    }
}
