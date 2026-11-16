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
