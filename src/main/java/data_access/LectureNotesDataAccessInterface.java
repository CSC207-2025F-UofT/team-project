package data_access;

import java.util.List;

public interface LectureNotesDataAccessInterface {

    String generateLectureNotes(String courseId,
                                List<String> filePaths,
                                String topic)
            throws LectureNotesDataAccessException;
}

