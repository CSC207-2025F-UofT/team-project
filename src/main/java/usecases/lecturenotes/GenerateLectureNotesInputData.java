package usecases.lecturenotes;

import java.util.List;

public class GenerateLectureNotesInputData {

    private final String courseId;
    private final List<String> filePaths;
    private final String topic;

    public GenerateLectureNotesInputData(String courseId,
                                         List<String> filePaths,
                                         String topic) {
        this.courseId = courseId;
        this.filePaths = filePaths;
        this.topic = topic;
    }

    public String getCourseId() {
        return courseId;
    }

    public List<String> getFilePaths() {
        return filePaths;
    }

    public String getTopic() {
        return topic;
    }
}
