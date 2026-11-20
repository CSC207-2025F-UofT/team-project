package view.components;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import interface_adapter.load_photos.LoadPhotoController;
import interface_adapter.load_photos.LoadPhotoPresenter;
import interface_adapter.load_photos.PhotoImageViewModel;
import use_case.load_photos.LoadPhotoInputBoundary;
import use_case.load_photos.LoadPhotoOutputData;

import static org.junit.jupiter.api.Assertions.*;

class ThumbnailCacheTest {
    private static final int SIZE = 32;

    private PhotoImageViewModel viewModel;
    private LoadPhotoPresenter presenter;
    private AtomicInteger loads;

    @BeforeEach
    void setUp() {
        viewModel = new PhotoImageViewModel();
        presenter = new LoadPhotoPresenter(viewModel);
        loads = new AtomicInteger();
    }

    private ThumbnailCache cacheWith(LoadPhotoInputBoundary interactor) {
        return new ThumbnailCache(SIZE, 10, viewModel, new LoadPhotoController(interactor));
    }

    /** ThumbnailCache.get is EDT-only, so tests call it there and hand the result back. */
    private static <T> T onEdt(Supplier<T> task) throws Exception {
        AtomicReference<T> result = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> result.set(task.get()));
        return result.get();
    }

    private static Icon placeholder(ThumbnailCache cache) throws Exception {
        return onEdt(() -> cache.get(null, () -> { }));
    }

    private static byte[] png(int width, int height) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB), "png", out);
        return out.toByteArray();
    }

    @Test
    void nullIdReturnsPlaceholderWithoutLoading() throws Exception {
        ThumbnailCache cache = cacheWith(input -> loads.incrementAndGet());

        Icon icon = placeholder(cache);

        assertEquals(SIZE, icon.getIconWidth());
        assertEquals(0, loads.get());
    }

    @Test
    void loadsDecodesCropsAndCaches() throws Exception {
        byte[] image = png(64, 48);
        ThumbnailCache cache = cacheWith(input -> {
            loads.incrementAndGet();
            presenter.prepareSuccessView(new LoadPhotoOutputData(input.getPhotoId(), image));
        });
        CountDownLatch loaded = new CountDownLatch(1);

        Icon first = onEdt(() -> cache.get("p1", loaded::countDown));
        assertTrue(loaded.await(2, TimeUnit.SECONDS), "onLoaded never called");
        Icon second = onEdt(() -> cache.get("p1", () -> fail("cached icon should not reload")));

        assertSame(placeholder(cache), first);
        assertNotSame(placeholder(cache), second);
        assertEquals(SIZE, second.getIconWidth());     // 64x48 cropped to a square
        assertEquals(SIZE, second.getIconHeight());
        assertEquals(1, loads.get());
    }

    @Test
    void failedLoadCachesPlaceholderAndDoesNotRetry() throws Exception {
        ThumbnailCache cache = cacheWith(input -> {
            loads.incrementAndGet();
            presenter.prepareFailView(input.getPhotoId(), "gone");
        });
        CountDownLatch loaded = new CountDownLatch(1);

        onEdt(() -> cache.get("p1", loaded::countDown));
        assertTrue(loaded.await(2, TimeUnit.SECONDS));
        Icon after = onEdt(() -> cache.get("p1", () -> { }));

        assertSame(placeholder(cache), after);
        assertEquals(1, loads.get());
    }

    @Test
    void undecodableBytesBecomePlaceholder() throws Exception {
        ThumbnailCache cache = cacheWith(input ->
                presenter.prepareSuccessView(new LoadPhotoOutputData(input.getPhotoId(), new byte[] {1, 2, 3})));
        CountDownLatch loaded = new CountDownLatch(1);

        onEdt(() -> cache.get("p1", loaded::countDown));
        assertTrue(loaded.await(2, TimeUnit.SECONDS));

        assertSame(placeholder(cache), onEdt(() -> cache.get("p1", () -> { })));
    }

    @Test
    void concurrentRequestsShareOneLoad() throws Exception {
        CountDownLatch release = new CountDownLatch(1);
        byte[] image = png(8, 8);
        ThumbnailCache cache = cacheWith(input -> {
            loads.incrementAndGet();
            try {
                release.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            presenter.prepareSuccessView(new LoadPhotoOutputData(input.getPhotoId(), image));
        });
        CountDownLatch bothNotified = new CountDownLatch(2);

        onEdt(() -> {
            cache.get("p1", bothNotified::countDown);
            return cache.get("p1", bothNotified::countDown);    // second request while first is in flight
        });
        release.countDown();

        assertTrue(bothNotified.await(2, TimeUnit.SECONDS), "both callbacks should run");
        assertEquals(1, loads.get());
    }

    @Test
    void ignoresResultsItDidNotRequest() throws Exception {
        CountDownLatch requested = new CountDownLatch(1);
        ThumbnailCache cache = cacheWith(input -> requested.countDown());

        presenter.prepareSuccessView(new LoadPhotoOutputData("other", png(8, 8)));  // e.g. another view's load
        SwingUtilities.invokeAndWait(() -> { });                                    // let queued EDT work finish

        onEdt(() -> cache.get("other", () -> { }));
        assertTrue(requested.await(2, TimeUnit.SECONDS), "unrequested result must not be cached");
    }
}