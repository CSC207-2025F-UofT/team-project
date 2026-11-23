package use_case.save_scene;

import java.util.Objects;

public final class SaveSceneInputData {
    private final String filePath;
    private final String fileName;
    private final boolean overwrite;

    public SaveSceneInputData(String filePath, String fileName, boolean overwrite) {
        this.filePath = Objects.requireNonNull(filePath, "filePath");
        this.fileName = Objects.requireNonNull(fileName, "fileName");
        this.overwrite = overwrite;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public boolean mayOverwrite() {
        return overwrite;
    }
}
