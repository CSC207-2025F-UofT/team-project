import javax.imageio.*;
import javax.imageio.stream.ImageInputStream;
import org.w3c.dom.Node;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Duration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FFBE_API {

    // CHANGE HEROES HERE.
    private static final String[] HERO_IDS = {
            "100000102", // Rain
            "100000202", // Lasswell
            "100000302", // Fina
            "100000403", // Nichol
            "100000503"  // Lid
    };

    private static final String[] HERO_NAMES = {
            "Rain", "Lasswell", "Fina", "Nichol", "Lid"
    };

    // HP, attack, defense, speed.
    // Temporary values, not official FFBE stats.
    private static final int[][] HERO_STATS = {
            {160, 34, 24, 75},
            {140, 38, 20, 85},
            {125, 27, 18, 65},
            {130, 30, 21, 70},
            {150, 33, 25, 60}
    };

    // Verified simple monster animation sheets.
    private static final String[] MONSTER_POOL = {
            "10000000",
            "10000001",
            "10000002",
            "10000003",
            "10000004",
            "10000005"
    };

    private static final int ENEMY_COUNT = 3;

    private static final String ROOT =
            "https://raw.githubusercontent.com/"
                    + "DaddyRaegen/ffbe_asset_dump/main/";

    private final Path cache = Path.of("ffbe-cache");

    private final HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NORMAL)
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    public GameAssets load() throws Exception {
        GameAssets game = new GameAssets();

        game.background = image("battle_bg/battle_bg_0002_02.jpg");

        List<Battle.Fighter> heroes = new ArrayList<>();
        List<Battle.Fighter> enemies = new ArrayList<>();

        for (int i = 0; i < HERO_IDS.length; i++) {
            int[] s = HERO_STATS[i];

            Battle.Fighter fighter = new Battle.Fighter(
                    HERO_NAMES[i],
                    true,
                    s[0], s[1], s[2], s[3]
            );

            GameAssets.Art art = new GameAssets.Art();

            String prefix =
                    "animated_gifs/unit_" + HERO_IDS[i] + "_";

            art.idle = gif(prefix + "1idle_opac.gif");
            art.move = gif(prefix + "2move_opac.gif");
            art.attack = gif(prefix + "3atk_opac.gif");
            art.dead = gif(prefix + "dead_opac.gif");

            art.portrait = image(
                    "unit_icons/unit_icon_" + HERO_IDS[i] + ".png"
            );

            heroes.add(fighter);
            game.art.put(fighter, art);
        }

        List<String> pool =
                new ArrayList<>(List.of(MONSTER_POOL));

        Collections.shuffle(pool);

        for (int i = 0; i < ENEMY_COUNT; i++) {
            String id = pool.get(i);

            Battle.Fighter fighter = new Battle.Fighter(
                    "Monster " + (i + 1),
                    false,
                    105 + i * 12,
                    25 + i * 2,
                    18 + i * 2,
                    55 + i * 12
            );

            BufferedImage sheet = image(
                    "monster_animated/monster_anime_" + id + ".png"
            );

            List<int[]> parts = csv(
                    "monster_animated_csv/monster_cgg_" + id + ".csv"
            );

            GameAssets.Art art = new GameAssets.Art();

            art.idle = monster(sheet, parts, id, "idle");
            art.attack = monster(sheet, parts, id, "atk");

            // These monsters use their idle pose while approaching.
            art.move = art.idle;
            art.mirror = false;

            enemies.add(fighter);
            game.art.put(fighter, art);
        }

        game.battle = new Battle(heroes, enemies);

        return game;
    }

    private byte[] bytes(String path) throws Exception {
        Path file = cache.resolve(path);

        if (Files.exists(file)) {
            return Files.readAllBytes(file);
        }

        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(ROOT + path))
                .timeout(Duration.ofSeconds(45))
                .GET()
                .build();

        HttpResponse<byte[]> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofByteArray()
        );

        if (response.statusCode() != 200) {
            throw new IOException(
                    "HTTP " + response.statusCode() + ": " + path
            );
        }

        Files.createDirectories(file.getParent());
        Files.write(file, response.body());

        return response.body();
    }

    private BufferedImage image(String path) throws Exception {
        BufferedImage image = ImageIO.read(
                new ByteArrayInputStream(bytes(path))
        );

        if (image == null) {
            throw new IOException("Invalid image: " + path);
        }

        return image;
    }

    // Reads individual GIF frames and their timings.
    // This lets attacks play once instead of looping forever.
    private GameAssets.Clip gif(String path) throws Exception {
        try (ImageInputStream input =
                     ImageIO.createImageInputStream(
                             new ByteArrayInputStream(bytes(path)))) {

            ImageReader reader =
                    ImageIO.getImageReadersByFormatName("gif").next();

            try {
                reader.setInput(input);

                Node screen = child(
                        reader.getStreamMetadata().getAsTree(
                                "javax_imageio_gif_stream_1.0"),
                        "LogicalScreenDescriptor"
                );

                BufferedImage canvas = new BufferedImage(
                        value(screen, "logicalScreenWidth"),
                        value(screen, "logicalScreenHeight"),
                        BufferedImage.TYPE_INT_ARGB
                );

                int count = reader.getNumImages(true);

                BufferedImage[] frames =
                        new BufferedImage[count];

                int[] delays = new int[count];

                for (int i = 0; i < count; i++) {
                    Node meta = reader.getImageMetadata(i)
                            .getAsTree(
                                    "javax_imageio_gif_image_1.0"
                            );

                    Node desc = child(meta, "ImageDescriptor");

                    Node control = child(
                            meta, "GraphicControlExtension"
                    );

                    BufferedImage previous = copy(canvas);

                    int x = value(desc, "imageLeftPosition");
                    int y = value(desc, "imageTopPosition");

                    Graphics2D g = canvas.createGraphics();
                    g.drawImage(reader.read(i), x, y, null);
                    g.dispose();

                    frames[i] = copy(canvas);

                    delays[i] = Math.max(
                            20,
                            value(control, "delayTime") * 10
                    );

                    String disposal = control.getAttributes()
                            .getNamedItem("disposalMethod")
                            .getNodeValue();

                    if (disposal.equals(
                            "restoreToBackgroundColor")) {

                        g = canvas.createGraphics();
                        g.setComposite(AlphaComposite.Clear);

                        g.fillRect(
                                x, y,
                                value(desc, "imageWidth"),
                                value(desc, "imageHeight")
                        );

                        g.dispose();

                    } else if (disposal.equals(
                            "restoreToPrevious")) {

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

    private static BufferedImage copy(BufferedImage source) {
        BufferedImage result = new BufferedImage(
                source.getWidth(),
                source.getHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = result.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();

        return result;
    }

    private static Node child(Node parent, String name) {
        for (Node n = parent.getFirstChild();
             n != null;
             n = n.getNextSibling()) {

            if (n.getNodeName().equals(name)) {
                return n;
            }
        }

        throw new IllegalArgumentException(
                "Missing GIF field: " + name
        );
    }

    private static int value(Node node, String attribute) {
        return Integer.parseInt(
                node.getAttributes()
                        .getNamedItem(attribute)
                        .getNodeValue()
        );
    }

    private List<int[]> csv(String path) throws Exception {
        List<int[]> rows = new ArrayList<>();

        String text = new String(
                bytes(path),
                StandardCharsets.UTF_8
        );

        for (String line : text.split("\\R")) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] fields = line.trim().split(",");
            int[] values = new int[fields.length];

            for (int i = 0; i < fields.length; i++) {
                values[i] =
                        Integer.parseInt(fields[i].trim());
            }

            rows.add(values);
        }

        return rows;
    }

    // Assembles the six selected monsters from their sheet parts.
    private GameAssets.Clip monster(
            BufferedImage sheet,
            List<int[]> parts,
            String id,
            String action) throws Exception {

        List<int[]> sequence = csv(
                "monster_animated_csv/monster_"
                        + action + "_cgs_" + id + ".csv"
        );

        BufferedImage[] frames =
                new BufferedImage[sequence.size()];

        int[] delays = new int[frames.length];

        for (int i = 0; i < frames.length; i++) {
            int[] step = sequence.get(i);
            int[] row = parts.get(step[0]);

            BufferedImage frame = new BufferedImage(
                    512, 512,
                    BufferedImage.TYPE_INT_ARGB
            );

            Graphics2D g = frame.createGraphics();

            for (int p = row[1] - 1; p >= 0; p--) {
                int k = 2 + p * 11;

                int x = 256 + step[1] + row[k];
                int y = 256 + step[2] + row[k + 1];

                int sx = row[k + 6];
                int sy = row[k + 7];

                int w = row[k + 8];
                int h = row[k + 9];

                g.drawImage(
                        sheet,
                        x, y, x + w, y + h,
                        sx, sy, sx + w, sy + h,
                        null
                );
            }

            g.dispose();

            frames[i] = frame;

            delays[i] = Math.max(
                    20,
                    (int) Math.round(
                            step[3] * 1000.0 / 120 // enemy animation speed, increase divisor to +
                    )
            );
        }

        return new GameAssets.Clip(
                frames, delays, 256, 256
        );
    }
}