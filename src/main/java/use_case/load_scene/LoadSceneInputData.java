package use_case.load_scene;

import java.util.Objects;

public final class LoadSceneInputData {
    private final String filePath;
    private final String fileName;

    public LoadSceneInputData(String filePath, String fileName) {
        this.filePath = Objects.requireNonNull(filePath, "filePath");
        this.fileName = Objects.requireNonNull(fileName, "fileName");
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }
}
