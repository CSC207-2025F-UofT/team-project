package view.glass;

import org.junit.jupiter.api.Test;

import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** More than one notice can queue, reflow and dismiss without interfering with other kinds. */
class GlassNoticeStackTest {
    @Test
    void errorsAndSuccessesShareOneTopStackAndDoNotReplaceOneAnother() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JRootPane root = root();
            var error = GlassNotices.showError(root, "Can't calculate", "a has no value");
            GlassNotices.showSuccess(root, "Saved A");
            GlassNotices.showSuccess(root, "Saved B");
            var stack = stack(root);
            assertEquals(3, stack.activeCount());
            List<Integer> positions = stack.targetPositions();
            assertEquals(105, positions.get(0));
            assertTrue(positions.get(0) < positions.get(1));
            assertTrue(positions.get(1) < positions.get(2));
            error.dismiss();
            assertEquals(2, stack.activeCount());
            assertEquals(105, stack.targetPositions().get(0), "remaining notifications move to the top");
            for (int i = 0; i < 45; i++) stack.tick();
            assertEquals(105, stack.targetPositions().get(0));
            assertEquals(2, stack.activeCount(), "the remaining items survive while the old one fades out");
        });
    }

    @Test
    void manualDismissIsIdempotentAndAllNoticesHaveTwoSecondExpiry() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JRootPane root = root();
            var first = GlassNotices.showError(root, "Error", "Detail");
            var second = GlassNotices.showSuccess(root, "Saved");
            var stack = stack(root);
            assertEquals(2_000, GlassNotices.DEFAULT_DURATION_MS);
            first.dismiss();
            first.dismiss();
            assertEquals(1, stack.activeCount());
            second.dismiss();
            assertEquals(0, stack.activeCount());
        });
    }

    @Test
    void errorStaysButSuccessFades() throws Exception {
        java.util.concurrent.atomic.AtomicReference<JRootPane> root = new java.util.concurrent.atomic.AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            root.set(root());
            GlassNotices.showError(root.get(), "Can't calculate", "a has no value");
            GlassNotices.showSuccess(root.get(), "Saved");
        });
        Thread.sleep(GlassNotices.SUCCESS_DURATION_MS + 400);
        SwingUtilities.invokeAndWait(() -> assertEquals(1, stack(root.get()).activeCount(), "only the error is left"));
    }

    @Test
    void progressStaysTillDismissedAndHasNoX() throws Exception {
        java.util.concurrent.atomic.AtomicReference<JRootPane> root = new java.util.concurrent.atomic.AtomicReference<>();
        java.util.concurrent.atomic.AtomicReference<GlassNotices.NoticeHandle> card = new java.util.concurrent.atomic.AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            root.set(root());
            card.set(GlassNotices.showProgress(root.get(), "Calculating 1+1…"));
            var view = new GlassNotices.NoticeView(GlassNotices.Kind.PROGRESS, "", "Calculating 1+1…");
            assertEquals(0, view.getComponentCount(), "nothing to close");
        });
        Thread.sleep(GlassNotices.SUCCESS_DURATION_MS + 400);
        SwingUtilities.invokeAndWait(() -> {
            assertEquals(1, stack(root.get()).activeCount(), "still working");
            card.get().dismiss();
            assertEquals(0, stack(root.get()).activeCount());
        });
    }

    @Test
    void pillMakesRoomForAFormulaAndShrinksTallOnes() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            javax.swing.Icon tall = new javax.swing.Icon() {
                @Override public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) { }
                @Override public int getIconWidth() { return 120; }
                @Override public int getIconHeight() { return 68; }
            };
            var words = new GlassNotices.NoticeView(GlassNotices.Kind.PROGRESS, "", "Calculating");
            var both = new GlassNotices.NoticeView(GlassNotices.Kind.PROGRESS, "", "Calculating", tall);
            int extra = both.getPreferredSize().width - words.getPreferredSize().width;
            assertTrue(extra >= 60 && extra < 120, "68 tall gets halved, width " + extra);
            assertEquals(64, both.getPreferredSize().height, "pill stays one size");
        });
    }

    @Test
    void stackReflowsWhenTheHostWindowChangesWidth() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JRootPane root = root();
            GlassNotices.showError(root, "Error", "Detail");
            var stack = stack(root);
            int previous = stack.targetPositions().get(0);
            root.getLayeredPane().setSize(520, 650);
            stack.arrange();
            assertEquals(1, stack.activeCount());
            assertEquals(previous, stack.targetPositions().get(0));
        });
    }

    private static JRootPane root() {
        JRootPane root = new JRootPane();
        root.setSize(900, 650);
        root.getLayeredPane().setSize(900, 650);
        return root;
    }

    private static GlassNotices.NoticeStack stack(JRootPane root) {
        return (GlassNotices.NoticeStack) root.getClientProperty("mathlab.activeNotices");
    }
}
