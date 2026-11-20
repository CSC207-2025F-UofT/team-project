import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.w3c.dom.Node;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.nio.file.Files;
import java.nio.file.Path;

import java.time.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * Artwork: Pixel-boy / Sparklin Labs, CC0.
 * https://github.com/sparklinlabs/superpowers-asset-packs
 */
public class Game_API {

    private static final String ROOT =
            "https://raw.githubusercontent.com/"
                    + "sparklinlabs/superpowers-asset-packs/"
                    + "master/rpg-battle-system/";

    private static final Path CACHE =
            Path.of("superpowers-cache");

    // Set to 1–24 for a specific background.
    // Set to 0 to choose randomly at startup.
    private static final int BACKGROUND = 0;

    private static final String[] HERO_FOLDERS = {
            "warrior",
            "paladin",
            "mage",
            "bowman",
            "Wizard"
    };

    private static final String[] HERO_NAMES = {
            "Warrior",
            "Paladin",
            "Mage",
            "Bowman",
            "Wizard"
    };

    // HP, attack, defense, speed.
    private static final int[][] HERO_STATS = {
            {160, 34, 24, 75},
            {180, 29, 32, 55},
            {125, 40, 18, 70},
            {135, 33, 20, 90},
            {130, 38, 19, 65}
    };

    private static final String[] ENEMY_FOLDERS = {
            "slime",
            "bat",
            "mushroom"
    };

    private static final String[] ENEMY_NAMES = {
            "Slime",
            "Bat",
            "Mushroom"
    };

    private static final int[][] ENEMY_STATS = {
            {145, 27, 21, 50},
            {120, 30, 18, 82},
            {160, 29, 24, 60}
    };

    private final HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    public GameAssets load() throws Exception {
        GameAssets game = new GameAssets();

        int background = BACKGROUND == 0
                ? ThreadLocalRandom.current().nextInt(1, 25)
                : BACKGROUND;

        if (background < 1 || background > 24) {
            throw new IllegalArgumentException(
                    "BACKGROUND must be 0 or between 1 and 24."
            );
        }

        game.background = image(
                "backgrounds/" + background + ".png"
        );

        List<Battle.Fighter> heroes = new ArrayList<>();
        List<Battle.Fighter> enemies = new ArrayList<>();

        for (int i = 0; i < HERO_FOLDERS.length; i++) {
            Battle.Fighter fighter = fighter(
                    HERO_NAMES[i], true, HERO_STATS[i]
            );

            GameAssets.Art art = loadArt(
                    "char/" + HERO_FOLDERS[i] + "/"
            );

            // Heroes face toward the enemies on the left.
            art.mirror = false;

            heroes.add(fighter);
            game.art.put(fighter, art);
        }

        for (int i = 0; i < ENEMY_FOLDERS.length; i++) {
            Battle.Fighter fighter = fighter(
                    ENEMY_NAMES[i], false, ENEMY_STATS[i]
            );

            GameAssets.Art art = loadArt(
                    "monster/" + ENEMY_FOLDERS[i] + "/"
            );

            // These enemy sprites face toward the right.
            art.mirror = false;

            enemies.add(fighter);
            game.art.put(fighter, art);
        }

        game.battle = new Battle(heroes, enemies);

        return game;
    }

    private Battle.Fighter fighter(
            String name,
            boolean player,
            int[] stats) {

        return new Battle.Fighter(
                name,
                player,
                stats[0],
                stats[1],
                stats[2],
                stats[3]
        );
    }

    private GameAssets.Art loadArt(String folder)
            throws Exception {

        GameAssets.Art art = new GameAssets.Art();

        art.idle = gif(folder + "idle.gif");
        art.attack = gif(folder + "attack.gif");

        // The current view moves the sprite across the screen.
        art.move = art.idle;

        // Null tells the current view to fade defeated fighters.
        art.dead = null;

        // Use a cropped idle frame as the portrait.
        BufferedImage first = art.idle.frames[0];
        Rectangle bounds = GameAssets.bounds(first);

        art.portrait = copy(first.getSubimage(
                bounds.x,
                bounds.y,
                bounds.width,
                bounds.height
        ));

        return art;
    }

    private byte[] bytes(String relativePath)
            throws Exception {

        Path file = CACHE.resolve(relativePath);

        if (Files.isRegularFile(file)) {
            return Files.readAllBytes(file);
        }

        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(ROOT + relativePath))
                .timeout(Duration.ofSeconds(45))
                .header("User-Agent", "CSC207-RPG")
                .GET()
                .build();

        HttpResponse<byte[]> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofByteArray()
        );

        if (response.statusCode() != 200) {
            throw new IOException(
                    "Could not download " + relativePath
                            + " (HTTP "
                            + response.statusCode() + ")"
            );
        }

        byte[] data = response.body();

        // Validate before caching an image response.
        try (ByteArrayInputStream input =
                     new ByteArrayInputStream(data)) {

            if (ImageIO.read(input) == null) {
                throw new IOException(
                        "Invalid image: " + relativePath
                );
            }
        }

        Files.createDirectories(file.getParent());

        // A cache write failure should not prevent playing.
        try {
            Files.write(file, data);
        } catch (IOException exception) {
            System.err.println(
                    "Could not cache " + relativePath
                            + ": " + exception.getMessage()
            );
        }

        return data;
    }

    private BufferedImage image(String relativePath)
            throws Exception {

        BufferedImage result = ImageIO.read(
                new ByteArrayInputStream(bytes(relativePath))
        );

        if (result == null) {
            throw new IOException(
                    "Invalid image: " + relativePath
            );
        }

        return result;
    }

    /**
     * Composes GIF frames and preserves their frame timings.
     */
    private GameAssets.Clip gif(String relativePath)
            throws Exception {

        try (ImageInputStream input =
                     ImageIO.createImageInputStream(
                             new ByteArrayInputStream(
                                     bytes(relativePath)))) {

            if (input == null) {
                throw new IOException(
                        "Cannot read GIF: " + relativePath
                );
            }

            ImageReader reader =
                    ImageIO.getImageReadersByFormatName("gif")
                            .next();

            try {
                reader.setInput(input);

                Node streamMetadata =
                        reader.getStreamMetadata().getAsTree(
                                "javax_imageio_gif_stream_1.0"
                        );

                Node screen = child(
                        streamMetadata,
                        "LogicalScreenDescriptor"
                );

                int width = value(
                        screen, "logicalScreenWidth", 1
                );

                int height = value(
                        screen, "logicalScreenHeight", 1
                );

                BufferedImage canvas = new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_ARGB
                );

                int count = reader.getNumImages(true);

                if (count == 0) {
                    throw new IOException(
                            "Empty GIF: " + relativePath
                    );
                }

                BufferedImage[] frames =
                        new BufferedImage[count];

                int[] delays = new int[count];

                for (int i = 0; i < count; i++) {
                    Node metadata =
                            reader.getImageMetadata(i)
                                    .getAsTree(
                                            "javax_imageio_gif_image_1.0"
                                    );

                    Node descriptor = child(
                            metadata, "ImageDescriptor"
                    );

                    Node control = child(
                            metadata, "GraphicControlExtension"
                    );

                    int x = value(
                            descriptor, "imageLeftPosition", 0
                    );

                    int y = value(
                            descriptor, "imageTopPosition", 0
                    );

                    int frameWidth = value(
                            descriptor, "imageWidth", width
                    );

                    int frameHeight = value(
                            descriptor, "imageHeight", height
                    );

                    BufferedImage previous = copy(canvas);

                    Graphics2D graphics =
                            canvas.createGraphics();

                    graphics.drawImage(
                            reader.read(i), x, y, null
                    );

                    graphics.dispose();

                    BufferedImage frame = copy(canvas);

                    // The preview GIFs have a flat background.
                    // Remove only matching pixels connected
                    // to the image edges.
                    removeBackground(frame);

                    frames[i] = frame;

                    delays[i] = Math.max(
                            20,
                            value(control, "delayTime", 10) * 10
                    );

                    String disposal = attribute(
                            control,
                            "disposalMethod",
                            "none"
                    );

                    if ("restoreToBackgroundColor"
                            .equals(disposal)) {

                        graphics = canvas.createGraphics();
                        graphics.setComposite(
                                AlphaComposite.Clear
                        );

                        graphics.fillRect(
                                x, y, frameWidth, frameHeight
                        );

                        graphics.dispose();

                    } else if ("restoreToPrevious"
                            .equals(disposal)) {

                        canvas = previous;
                    }
                }

                Rectangle body =
                        GameAssets.bounds(frames[0]);

                return new GameAssets.Clip(
                        frames,
                        delays,
                        body.getCenterX(),
                        body.getMaxY()
                );

            } finally {
                reader.dispose();
            }
        }
    }

    /**
     * Removes a flat GIF background without deleting enclosed
     * areas of the same colour inside the character.
     */
    private static void removeBackground(
            BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();

        int colour = image.getRGB(0, 0);

        if ((colour >>> 24) == 0) {
            return;
        }

        // Only remove a uniform backdrop present at all corners.
        if (image.getRGB(width - 1, 0) != colour
                || image.getRGB(0, height - 1) != colour
                || image.getRGB(
                width - 1, height - 1
        ) != colour) {
            return;
        }

        int[] queue = new int[width * height];
        int head = 0;
        int tail = 0;

        // Seed every matching border pixel.
        for (int x = 0; x < width; x++) {
            tail = enqueue(
                    image, x, 0, colour, queue, tail
            );

            tail = enqueue(
                    image, x, height - 1,
                    colour, queue, tail
            );
        }

        for (int y = 0; y < height; y++) {
            tail = enqueue(
                    image, 0, y, colour, queue, tail
            );

            tail = enqueue(
                    image, width - 1, y,
                    colour, queue, tail
            );
        }

        while (head < tail) {
            int index = queue[head++];

            int x = index % width;
            int y = index / width;

            if (x > 0) {
                tail = enqueue(
                        image, x - 1, y,
                        colour, queue, tail
                );
            }

            if (x + 1 < width) {
                tail = enqueue(
                        image, x + 1, y,
                        colour, queue, tail
                );
            }

            if (y > 0) {
                tail = enqueue(
                        image, x, y - 1,
                        colour, queue, tail
                );
            }

            if (y + 1 < height) {
                tail = enqueue(
                        image, x, y + 1,
                        colour, queue, tail
                );
            }
        }
    }

    private static int enqueue(
            BufferedImage image,
            int x,
            int y,
            int colour,
            int[] queue,
            int tail) {

        if (image.getRGB(x, y) == colour) {
            image.setRGB(x, y, 0);
            queue[tail++] = y * image.getWidth() + x;
        }

        return tail;
    }

    private static BufferedImage copy(
            BufferedImage source) {

        BufferedImage result = new BufferedImage(
                source.getWidth(),
                source.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D graphics = result.createGraphics();
        graphics.drawImage(source, 0, 0, null);
        graphics.dispose();

        return result;
    }

    private static Node child(
            Node parent,
            String name) {

        if (parent == null) {
            return null;
        }

        for (Node node = parent.getFirstChild();
             node != null;
             node = node.getNextSibling()) {

            if (name.equals(node.getNodeName())) {
                return node;
            }
        }

        return null;
    }

    private static String attribute(
            Node node,
            String name,
            String fallback) {

        if (node == null || node.getAttributes() == null) {
            return fallback;
        }

        Node result =
                node.getAttributes().getNamedItem(name);

        return result == null
                ? fallback
                : result.getNodeValue();
    }

    private static int value(
            Node node,
            String name,
            int fallback) {

        return Integer.parseInt(
                attribute(
                        node,
                        name,
                        Integer.toString(fallback)
                )
        );
    }
}
