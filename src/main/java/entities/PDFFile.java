package entities;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PDFFile {
    private final Path path;

    public PDFFile(String path) {
        this.path = Paths.get(path);
    }

    public Path getPath() {
        return path;
    }
}
