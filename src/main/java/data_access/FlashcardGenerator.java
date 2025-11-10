package data_access;

import entities.FlashcardSet;

public interface FlashcardGenerator {
    FlashcardSet generateForCourse(String courseName, String content);
}