package org.accessgraph.data_access;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Stores only provider tiles and their HTTP cache metadata. Each tile is replaced atomically. */
final class MapTileCache {
    static final int MAX_TILE_BYTES = 512 * 1024;
    private static final long DEFAULT_TTL = TimeUnit.DAYS.toSeconds(7);
    private static final Pattern MAX_AGE = Pattern.compile("(?:^|,)\\s*max-age=\"?(\\d+)");
    private final Path directory;

    MapTileCache(Path directory) { this.directory = directory; }

    Entry read(String tile) {
        try (DataInputStream input = new DataInputStream(Files.newInputStream(directory.resolve(tile)))) {
            long expiresAt = input.readLong();
            String cacheControl = input.readUTF();
            String etag = input.readUTF();
            String lastModified = input.readUTF();
            byte[] image = input.readNBytes(MAX_TILE_BYTES + 1);
            if (image.length == 0 || image.length > MAX_TILE_BYTES) { return null; }
            return new Entry(expiresAt, cacheControl, etag, lastModified, image);
        } catch (IOException ignored) {
            return null;
        }
    }

    void store(String tile, HttpHeaders headers, byte[] image, Entry previous) {
        String cacheControl = headers.allValues("Cache-Control").isEmpty() && previous != null
                ? previous.cacheControl : String.join(",", headers.allValues("Cache-Control"));
        String directives = cacheControl.toLowerCase(Locale.ROOT);
        Path target = directory.resolve(tile);
        try {
            if (hasDirective(directives, "no-store")) {
                Files.deleteIfExists(target);
                return;
            }
            String etag = headers.firstValue("ETag").orElse(previous == null ? "" : previous.etag);
            String modified = headers.firstValue("Last-Modified").orElse(previous == null ? "" : previous.lastModified);
            Files.createDirectories(directory);
            Path temporary = Files.createTempFile(directory, "tile-", ".tmp");
            try {
                try (DataOutputStream output = new DataOutputStream(Files.newOutputStream(temporary))) {
                    output.writeLong(expiration(headers, directives));
                    output.writeUTF(cacheControl);
                    output.writeUTF(etag);
                    output.writeUTF(modified);
                    output.write(image);
                }
                Files.move(temporary, target, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
            } finally {
                Files.deleteIfExists(temporary);
            }
        } catch (IOException ignored) {
            // A full or read-only cache must not prevent the current tile from displaying.
        }
    }

    private static long expiration(HttpHeaders headers, String directives) {
        long now = Instant.now().getEpochSecond();
        if (hasDirective(directives, "no-cache")) { return now; }
        Matcher maxAge = MAX_AGE.matcher(directives);
        if (maxAge.find()) {
            long lifetime = duration(maxAge.group(1), DEFAULT_TTL);
            long age = duration(headers.firstValue("Age").orElse(""), 0);
            return now + Math.max(0, lifetime - age);
        }
        return headers.firstValue("Expires").map(value -> {
            try {
                return ZonedDateTime.parse(value, DateTimeFormatter.RFC_1123_DATE_TIME).toEpochSecond();
            } catch (DateTimeParseException ignored) {
                return now + DEFAULT_TTL;
            }
        }).orElse(now + DEFAULT_TTL);
    }

    private static boolean hasDirective(String directives, String name) {
        for (String directive : directives.split(",")) {
            if (directive.strip().split("=", 2)[0].equals(name)) { return true; }
        }
        return false;
    }

    private static long duration(String value, long fallback) {
        try {
            return Math.max(0, Math.min(Integer.MAX_VALUE, Long.parseLong(value)));
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    static final class Entry {
        private final long expiresAt;
        private final String cacheControl;
        final String etag;
        final String lastModified;
        final byte[] image;

        private Entry(long expiresAt, String cacheControl, String etag, String lastModified, byte[] image) {
            this.expiresAt = expiresAt;
            this.cacheControl = cacheControl;
            this.etag = etag;
            this.lastModified = lastModified;
            this.image = image;
        }

        boolean isFresh() { return expiresAt > Instant.now().getEpochSecond(); }
    }
}
