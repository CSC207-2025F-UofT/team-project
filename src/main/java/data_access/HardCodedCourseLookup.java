package data_access;

import entities.Course;
import entities.PDFFile;

public class HardCodedCourseLookup {

    private final Course demoCourse;

    public HardCodedCourseLookup() {

        this.demoCourse = new Course("RLG200");

        // Only the filename. Must be inside src/main/resources/
        this.demoCourse.addFile(new PDFFile("test.pdf"));
    }

    public Course getCourseById(String courseId) {
        if (demoCourse.getCourseId().equals(courseId)) {
            return demoCourse;
        }
        return null;
    }
}