package usecase.Resubmit;

import java.io.File;
import java.time.*;

public class ResubmitInputData {
    private final LocalTime time;
    private final File selectedFile;

    public ResubmitInputData(LocalTime time, File selectedFile){
        this.time = time;
        this.selectedFile = selectedFile;
    }

    public LocalTime getTime() {
        return time;
    }

    public File getSelectedFile() {
        return selectedFile;
    }
}
