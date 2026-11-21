package data_access.image;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;

import entity.Photo;
import entity.ImageType;
import entity.Location;
import use_case.import_photos.ImageImportException;
import use_case.import_photos.ImageImportProgressData;
import use_case.import_photos.ImageImporterInterface;

public class ImageImporter implements ImageImporterInterface {
    private static final List<DateTimeFormatter> FORMATTERS = List.of(
        DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
        DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm"),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    );

    private final ImageResolver resolver;

    public ImageImporter(ImageResolver resolver) {
        this.resolver = resolver;
    }

    private static Optional<Photo> getPhotoFromPath(Path imagePath) {
        Optional<ImageType> type = ImageResolver.getImageType(imagePath);
        if (type.isEmpty()) return Optional.empty();

        try {
            File imageFile = imagePath.toFile();
            Metadata metadata = ImageMetadataReader.readMetadata(imageFile);

            Location location = null;
            LocalDateTime time = null;

            GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);

            if (gpsDirectory != null && gpsDirectory.getGeoLocation() != null) {
                GeoLocation geoLocation = gpsDirectory.getGeoLocation();

                if (!geoLocation.isZero()) {
                    location = new Location(geoLocation.getLatitude(), geoLocation.getLongitude(), null);
                }
            }

            ExifSubIFDDirectory exifDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

            if (exifDirectory != null) {
                String dateTimeString = exifDirectory.getString(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);

                for (DateTimeFormatter formatter : FORMATTERS) {
                    try {
                        time = LocalDateTime.parse(dateTimeString, formatter);
                        break;
                    } catch (DateTimeParseException e) {}
                }
            }

            String id = UUID.randomUUID().toString();
            Photo photo = new Photo(id, type.get(), location, time);

            return Optional.of(photo);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private boolean saveImage(Photo photo, Path imagePath) {
        try {
            Path copyPath = resolver.getImagePath(photo);

            Files.createDirectories(copyPath.getParent());
            Files.copy(imagePath, copyPath, StandardCopyOption.REPLACE_EXISTING);

            return true;

        } catch (IOException e) {
            return false;
        }
    }

    private void deleteImage(Photo photo) {
        try {
            Path imagePath = resolver.getImagePath(photo);

            Files.deleteIfExists(imagePath);

        } catch (IOException e) {}
    }

    @Override
    public List<Photo> importImages(List<String> paths, Consumer<ImageImportProgressData> onImageProcessed) {
        List<Path> imagePaths = new ArrayList<>();

        for (String pathString : paths) {
            try {
                Path path = Path.of(pathString);

                if (Files.isDirectory(path)) {
                    try (Stream<Path> pathStream = Files.walk(path)) {
                        pathStream
                            .filter(p -> Files.isRegularFile(p) && ImageResolver.isSupportedType(p))
                            .forEach(p -> imagePaths.add(p));
                    }

                } else if (Files.isRegularFile(path) && ImageResolver.isSupportedType(path)) {
                    imagePaths.add(path);
                }

            } catch (InvalidPathException | IOException | UncheckedIOException e) {
                throw new ImageImportException(pathString, e.getMessage(), e);
            }
        }

        List<Photo> photos = new ArrayList<>();

        for (Path imagePath : imagePaths) {
            Optional<Photo> photo = getPhotoFromPath(imagePath);
            if (photo.isEmpty()) continue;

            boolean successful = saveImage(photo.get(), imagePath);

            onImageProcessed.accept(
                new ImageImportProgressData(imagePath.toString(), successful, imagePaths.size())
            );

            photos.add(photo.get());
        }

        return photos;
    }

    @Override
    public void cleanupImages(List<Photo> photos) {
        for (Photo photo : photos) {
            deleteImage(photo);
        }
    }
}
