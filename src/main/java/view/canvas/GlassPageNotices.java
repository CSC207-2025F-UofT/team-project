package view.canvas;

import view.glass.GlassNotices;

import javax.swing.Icon;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

// Notices on top of GlassNotices, keeps one error up at a time
final class GlassPageNotices implements Notices {
    private final Component parent;
    private GlassNotices.NoticeHandle errorCard;
    private String error;        // reason on show, null = none (tests peek)
    private String lastSuccess;
    private final List<String> working = new ArrayList<>();  // progress cards up

    GlassPageNotices(Component parent) {
        this.parent = parent;
    }

    @Override
    public void success(String words, String latex) {
        clearError();  // something worked, old error is stale
        lastSuccess = plain(words, latex, "");
        Icon formula = FormulaIcons.inline(latex);
        if (formula == null) {
            GlassNotices.showSuccess(parent, lastSuccess);
        } else {
            GlassNotices.showSuccess(parent, words, formula);
        }
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

    @Override
    public Runnable progress(String words, String latex) {
        String text = plain(words, latex, "…");
        Icon formula = FormulaIcons.inline(latex);
        GlassNotices.NoticeHandle card = formula == null
                ? GlassNotices.showProgress(parent, text)
                : GlassNotices.showProgress(parent, words, formula);
        working.add(text);
        boolean[] done = {false};
        return () -> {
            if (!done[0]) {  // safe to call twice
                done[0] = true;
                card.dismiss();
                working.remove(text);
            }
        };
    }

    // raw text version, fallback when latex won't typeset (tests read it too)
    private static String plain(String words, String latex, String end) {
        return (latex == null || latex.isBlank() ? words : words + " " + latex) + end;
    }

    String shownProgress() {
        return working.isEmpty() ? null : working.get(working.size() - 1);
    }

    String shownError() {
        return error;
    }

    String lastSuccess() {
        return lastSuccess;
    }
}
