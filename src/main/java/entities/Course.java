package entities;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseID;
    private List<PDFFile> uploadedFiles;

    public Course(String courseID) {
        this.courseID = courseID;
        this.uploadedFiles = new ArrayList<>();
    }

    public String getCourseId() {
        return courseID;
    }

    public void addFile(PDFFile file) {
        uploadedFiles.add(file);
    }

    public List<PDFFile> getUploadedFiles() {
        return uploadedFiles;
    }
}
