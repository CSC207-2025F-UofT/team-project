package data_access.image;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import entity.ImageType;
import entity.Photo;
import data_access.StorageResolver;

public class ImageResolver {
    private static final String PHOTOS_FOLDER_NAME = "photos";

    private static final Map<String, ImageType> EXTENSION_TO_TYPE_MAP = Map.of(
        "jpg", ImageType.JPEG,
        "jpeg", ImageType.JPEG,
        "png", ImageType.PNG,
        "webp", ImageType.WEBP
    );

    private static final Map<ImageType, String> TYPE_TO_EXTENSION_MAP = Map.of(
        ImageType.JPEG, "jpg",
        ImageType.PNG, "png",
        ImageType.WEBP, "webp"
    );

    private final StorageResolver storageResolver;

    public ImageResolver(StorageResolver storageResolver) {
        this.storageResolver = storageResolver;
    }

    private static String getImageTypeExtension(ImageType type) {
        return TYPE_TO_EXTENSION_MAP.get(type);
    }

    public static boolean isSupportedType(String extension) {
        return EXTENSION_TO_TYPE_MAP.containsKey(extension.toLowerCase());
    }

    public static Optional<ImageType> getImageType(String extension) {
        return Optional.ofNullable(EXTENSION_TO_TYPE_MAP.get(extension.toLowerCase()));
    }

    public static boolean isSupportedType(Path filePath) {
        return isSupportedType(getExtension(filePath));
    }

    public static Optional<ImageType> getImageType(Path filePath) {
        return getImageType(getExtension(filePath));
    }

    private static String getExtension(Path filePath) {
        String fileName = filePath.getFileName().toString();
        String extension = "";
        int dotIndex = fileName.lastIndexOf('.');

        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            extension = fileName.substring(dotIndex + 1);
        }

        return extension;
    }

    public Path getImagePath(Photo photo) {
        String fileName = photo.getId() + '.' + getImageTypeExtension(photo.getType());

        return storageResolver.getDirectory()
            .resolve(PHOTOS_FOLDER_NAME)
            .resolve(fileName);
    }
}
