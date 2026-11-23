package use_case.save_scene;

public final class SaveSceneOutputData {
    private final String filePath;
    private final String fileName;

    public SaveSceneOutputData(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }
}
