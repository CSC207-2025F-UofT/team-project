package org.accessgraph.view;

import org.accessgraph.interface_adapter.TileGateway;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Keeps recently drawn tiles decoded in memory and loads missing ones in the background, newest view first.
 * Call every method except the constructor on the Swing event dispatch thread.
 */
final class TileLoader {
    private static final int CAPACITY = 320;
    /** The OpenStreetMap tile policy allows at most two simultaneous downloads. */
    private static final int THREADS = 2;
    private final TileGateway gateway;
    private final Executor dispatch;
    private final Runnable changed;
    private final Map<Long, BufferedImage> memory = new LinkedHashMap<>(CAPACITY, 0.75f, true) {
        @Override protected boolean removeEldestEntry(Map.Entry<Long, BufferedImage> eldest) {
            return size() > CAPACITY;
        }
    };
    private final Set<Long> failed = new HashSet<>();
    private final Set<Long> queued = ConcurrentHashMap.newKeySet();
    private final LinkedBlockingDeque<Long> pending = new LinkedBlockingDeque<>();
    private volatile Set<Long> wanted = Set.of();

    TileLoader(TileGateway gateway, Executor dispatch, Runnable changed) {
        this.gateway = gateway;
        this.dispatch = dispatch;
        this.changed = changed;
        for (int index = 0; index < THREADS; index++) {
            Thread worker = new Thread(this::work, "accessgraph-tiles-" + index);
            worker.setDaemon(true);
            worker.start();
        }
    }

    static long key(int zoom, int x, int y) { return (long) zoom << 40 | (long) x << 20 | y; }

    BufferedImage get(int zoom, int x, int y) { return memory.get(key(zoom, x, y)); }

    /** Replaces the tiles on screen, most important first. Tiles that scrolled away are not downloaded. */
    void want(List<Long> keys) {
        wanted = Set.copyOf(keys);
        for (int index = keys.size() - 1; index >= 0; index--) {
            long key = keys.get(index);
            if (!memory.containsKey(key) && !failed.contains(key) && queued.add(key)) { pending.offerFirst(key); }
        }
    }

    boolean isLoading() { return queued.stream().anyMatch(wanted::contains); }
    boolean hasFailures() { return failed.stream().anyMatch(wanted::contains); }
    void retry() { failed.clear(); }

    void cancel() {
        wanted = Set.of();
        pending.clear();
    }

    private void work() {
        while (true) {
            long key;
            try {
                key = pending.takeFirst();
            } catch (InterruptedException exception) {
                return;
            }
            if (!wanted.contains(key)) {
                queued.remove(key);
                continue;
            }
            BufferedImage image = null;
            try {
                image = opaque(gateway.tile((int) (key >>> 40), (int) (key >>> 20 & 0xFFFFF), (int) (key & 0xFFFFF)));
            } catch (IOException | RuntimeException exception) {
                image = null;
            } catch (InterruptedException exception) {
                return;
            }
            BufferedImage loaded = image;
            dispatch.execute(() -> {
                if (loaded != null) { memory.put(key, loaded); } else { failed.add(key); }
                queued.remove(key);
                changed.run();
            });
        }
    }

    /** PNG tiles decode as indexed images, which Java 2D scales slowly. Convert once, off the UI thread. */
    private static BufferedImage opaque(BufferedImage image) {
        if (image.getType() == BufferedImage.TYPE_INT_RGB) { return image; }
        BufferedImage copy = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = copy.createGraphics();
        graphics.drawImage(image, 0, 0, null);
        graphics.dispose();
        return copy;
    }
}
