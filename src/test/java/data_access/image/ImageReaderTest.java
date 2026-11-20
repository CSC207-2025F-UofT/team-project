package data_access.image;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import data_access.StorageResolver;
import entity.ImageType;
import entity.Photo;
import use_case.ImageReadException;

import static org.junit.jupiter.api.Assertions.*;

class ImageReaderTest {
    @TempDir
    Path tempDir;

    private ImageResolver resolver;
    private ImageReader reader;

    @BeforeEach
    void setUp() {
        StorageResolver storage = new StorageResolver();
        storage.setDirectory(tempDir);
        resolver = new ImageResolver(storage);
        reader = new ImageReader(resolver);
    }

    @Test
    void readsBytesFromResolvedPath() throws IOException {
        Photo photo = new Photo("p1", ImageType.JPEG, null, null);
        Path path = resolver.getImagePath(photo);
        Files.createDirectories(path.getParent());
        Files.write(path, new byte[] {4, 5, 6});

        assertArrayEquals(new byte[] {4, 5, 6}, reader.readFullImage(photo));
    }

    @Test
    void missingFileThrowsWithPhotoId() {
        Photo photo = new Photo("ghost", ImageType.PNG, null, null);

        ImageReadException e = assertThrows(ImageReadException.class, () -> reader.readFullImage(photo));
        assertEquals("ghost", e.getPhotoId());
    }
}