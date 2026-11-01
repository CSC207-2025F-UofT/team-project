import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class GameAssets {

    public Battle battle;
    public BufferedImage background;

    public final Map<Battle.Fighter, Art> art = new HashMap<>();

    public static class Art {
        public Clip idle;
        public Clip move;
        public Clip attack;
        public Clip dead;

        public BufferedImage portrait;
        public boolean mirror;
    }

    public static class Clip {
        public final BufferedImage[] frames;
        public final int[] ends;
        public final int duration;

        public final double anchorX;
        public final double anchorY;

        public final int bodyHeight;

        public Clip(BufferedImage[] frames, int[] delays,
                    double x, double y) {
            this.frames = frames;
            this.anchorX = x;
            this.anchorY = y;

            ends = new int[delays.length];

            int total = 0;

            for (int i = 0; i < delays.length; i++) {
                total += Math.max(20, delays[i]);
                ends[i] = total;
            }

            duration = total;
            bodyHeight = Math.max(1, bounds(frames[0]).height);
        }

        public BufferedImage frame(long ms, boolean loop) {
            long t = loop
                    ? ms % duration
                    : Math.min(ms, duration - 1);

            for (int i = 0; i < ends.length; i++) {
                if (t < ends[i]) {
                    return frames[i];
                }
            }

            return frames[frames.length - 1];
        }

        public void draw(Graphics2D graphics,
                         double x, double y,
                         double scale, boolean mirror,
                         long ms, boolean loop) {
            Graphics2D g = (Graphics2D) graphics.create();

            g.translate(x, y);
            g.scale(mirror ? -scale : scale, scale);

            g.drawImage(
                    frame(ms, loop),
                    (int) -anchorX,
                    (int) -anchorY,
                    null
            );

            g.dispose();
        }
    }

    public static Rectangle bounds(BufferedImage image) {
        int left = image.getWidth();
        int top = image.getHeight();
        int right = -1;
        int bottom = -1;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if ((image.getRGB(x, y) >>> 24) > 20) {
                    left = Math.min(left, x);
                    top = Math.min(top, y);
                    right = Math.max(right, x);
                    bottom = Math.max(bottom, y);
                }
            }
        }

        return right < left
                ? new Rectangle(0, 0, 1, 1)
                : new Rectangle(
                left, top,
                right - left + 1,
                bottom - top + 1
        );
    }
}