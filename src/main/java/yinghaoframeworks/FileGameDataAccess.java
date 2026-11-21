package yinghaoframeworks;

import entity.GameResult;
import yinghaousecase.savegame.SaveGameDataAccess;

import java.io.*;

public class FileGameDataAccess implements SaveGameDataAccess {
    private final File saveDir;

    public FileGameDataAccess() {
        this(new File("saves"));
    }

    public FileGameDataAccess(File saveDir) {
        this.saveDir = saveDir;
    }

    private File fileFor(String slotName) {
        return new File(saveDir, slotName + ".result");
    }

    @Override
    public void save(GameResult result, String slotName) throws IOException {
        if (!saveDir.exists() && !saveDir.mkdirs()) {
            throw new IOException("Could not create save directory: " + saveDir);
        }
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new BufferedOutputStream(
                             new FileOutputStream(fileFor(slotName))))) {
            out.writeObject(result);
        }
    }

    @Override
    public GameResult load(String slotName) throws IOException, ClassNotFoundException {
        File file = fileFor(slotName);
        if (!file.exists()) {
            throw new IOException("No result file: " + file.getPath());
        }
        try (ObjectInputStream in =
                     new ObjectInputStream(new BufferedInputStream(
                             new FileInputStream(file)))) {
            Object obj = in.readObject();
            if (!(obj instanceof GameResult)) {
                throw new IOException("File does not contain a GameResult.");
            }
            return (GameResult) obj;
        }
    }
}