import org.json.JSONObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

abstract class JSONControl {
  private final String fileName;
  private JSONObject contents;

  public JSONControl (String file) throws IOException {
    this.fileName = file;
    try {
      String flatContents = Files.readString(Path.of("src/main/data/" + file));
      this.contents = new JSONObject(flatContents);
    } catch (IOException e) {
      System.out.println("File not accessible. Has " + file + " been removed or misnamed?");
      throw e;
    }
  }

  public String getFileName() {return this.fileName;}

  public JSONObject getContents() {return this.contents;}

  public void writeContents() throws IOException {
    try {
      Files.writeString(Path.of("src/main/data/", this.fileName), this.contents.toString(4));
    } catch (IOException e) {
      System.out.println("Unable to write to file. Has " + this.fileName + " been removed or misnamed?");
      throw e;}
    }
  }



