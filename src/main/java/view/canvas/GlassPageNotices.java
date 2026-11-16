package view.canvas;

import view.glass.GlassNotices;

import java.awt.Component;

// Notices on top of GlassNotices, keeps one error up at a time
final class GlassPageNotices implements Notices {
    private final Component parent;
    private GlassNotices.NoticeHandle errorCard;
    private String error;        // reason on show, null = none (tests peek)
    private String lastSuccess;

    GlassPageNotices(Component parent) {
        this.parent = parent;
    }

    @Override
    public void success(String text) {
        clearError();  // something worked, old error is stale
        lastSuccess = text;
        GlassNotices.showSuccess(parent, text);
    }

    @Override
    public void error(String title, String reason) {
        clearError();  // newer error replaces it, no pile up
        error = reason;
        errorCard = GlassNotices.showError(parent, title, reason);
    }

    @Override
    public void clearError() {
        if (errorCard != null) {
            errorCard.dismiss();
        }
        errorCard = null;
        error = null;
    }

    String shownError() {
        return error;
    }

    String lastSuccess() {
        return lastSuccess;
    }
}
