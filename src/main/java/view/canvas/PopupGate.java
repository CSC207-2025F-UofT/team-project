package view.canvas;

import view.answer.EqualsSpot;
import view.answer.Formula;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

// popup waits till the "=" is written (or a done comparison like 2x>5), lasso picks don't have to
final class PopupGate {
    private final Set<Long> picked = new HashSet<>();
    private final Predicate<String> question;  // read latex is a finished comparison

    PopupGate(Predicate<String> question) {
        this.question = question;
    }

    void picked(long formulaId) {
        picked.add(formulaId);
    }

    // latex null = not read / couldn't read
    boolean allows(Formula f, String latex) {
        if (f == null) {
            return false;
        }
        return picked.contains(f.getId()) || EqualsSpot.written(f.getStrokes(), f.getCharHeight(), latex)
                || latex != null && question.test(latex);
    }
}
