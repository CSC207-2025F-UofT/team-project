package view.components;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import interface_adapter.load_photos.LoadPhotoController;
import interface_adapter.load_photos.PhotoImageState;
import interface_adapter.load_photos.PhotoImageViewModel;
import view.SwingThread;

/**
 * Loads square, center-cropped thumbnails off the EDT and keeps the most recent ones in memory.
 * Call only from EDT (renderers and SwingWorker.done() both run through there). 
 */
public class ThumbnailCache implements PropertyChangeListener {
    private final int size;
    private final Icon placeholder;
    private final Map<String, Icon> cache;
    private final Map<String, List<Runnable>> waiting = new HashMap<>();
    private final LoadPhotoController controller;
    private final ExecutorService loader = Executors.newFixedThreadPool(2, task -> {
        Thread thread = new Thread(task, "thumbnail-loader");
        thread.setDaemon(true);
        return thread;
    });


    public ThumbnailCache(int size, int maxEntries,
                          PhotoImageViewModel viewModel, LoadPhotoController controller) {
        this.size = size;
        this.placeholder = new ImageIcon(solid(size, new Color(0xDDDDDD)));
        this.cache = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Icon> eldes) {
                return size() > maxEntries;
            }
        };
        this.controller = controller;
        viewModel.addPropertyChangeListener(this);
    }

    public int getSize() {
        return size;
    }

    /** EDT only. Retruns cached thumbnail, or the placeholder while it loads. */
    public Icon get(String photoId, Runnable onLoaded) {
        if (photoId == null) {
            return placeholder;
        }
        Icon cached = cache.get(photoId);
        if (cached != null) {
            return cached;
        }
        List<Runnable> callbacks = waiting.get(photoId);
        if (callbacks == null) {
            waiting.put(photoId, new ArrayList<>(List.of(onLoaded)));
            loader.submit(() -> controller.execute(photoId));
        } else {
            callbacks.add(onLoaded);
        }
        return placeholder;
    }

    private static BufferedImage cropToSquare(BufferedImage source, int size) {
        double scale = (double) size / Math.min(source.getWidth(), source.getHeight());
        int width = (int) Math.round(source.getWidth() * scale);
        int height = (int) Math.round(source.getHeight() * scale);

        BufferedImage result = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = result.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(source, (size - width) / 2, (size - height) / 2, width, height, null);
        g.dispose();
        return result;
    }

    private static BufferedImage solid(int size, Color color) {
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, size, size);
        g.dispose();
        return image;
    }

    @Override 
    public void propertyChange(PropertyChangeEvent evt) {
        PhotoImageState state = (PhotoImageState) evt.getNewValue();
        Icon icon = decode(state.getImageBytes());
        SwingThread.runOnEdt(() -> {
            List<Runnable> callbacks = waiting.remove(state.getPhotoId());
            if (callbacks == null) {
                return;
            }
            cache.put(state.getPhotoId(), icon);
            callbacks.forEach(Runnable::run);
        });
    }

    private Icon decode(byte[] bytes) {
        if (bytes == null) {
            return placeholder;
        }
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
            return image == null ? placeholder : new ImageIcon(cropToSquare(image, size));
        } catch (IOException e) {
            return placeholder;
        }
    }
}