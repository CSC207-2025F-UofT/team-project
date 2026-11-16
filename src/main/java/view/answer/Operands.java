package view.answer;

import java.util.ArrayList;
import java.util.List;

// what follows a √ or ^ in answer text: one symbol or a (...) group, same as ExprWriter writes them
final class Operands {
    // operand is text[mark + 1, end)
    static final class Span {
        final int mark;
        final int end;

        Span(int mark, int end) {
            this.mark = mark;
            this.end = end;
        }

        // text[from, to) sits inside the operand
        boolean holds(int from, int to) {
            return from > mark && to <= end;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Span && ((Span) o).mark == mark && ((Span) o).end == end;
        }

        @Override
        public int hashCode() {
            return 31 * mark + end;
        }

        @Override
        public String toString() {
            return "[" + mark + "," + end + ")";
        }
    }

    private Operands() {
    }

    static List<Span> after(String text, char mark) {
        List<Span> spans = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == mark) {
                int end = end(text, i + 1);
                if (end > i + 1) {
                    spans.add(new Span(i, end));
                }
            }
        }
        return spans;
    }

    private static int end(String text, int start) {
        if (start >= text.length()) {
            return start;
        }
        char first = text.charAt(start);
        int i = start + 1;
        if (first == '(') {
            for (int open = 1; i < text.length() && open > 0; i++) {
                open += text.charAt(i) == '(' ? 1 : text.charAt(i) == ')' ? -1 : 0;
            }
            return i;  // unclosed runs to the end
        }
        if (first == '−' && i < text.length() && Character.isDigit(text.charAt(i))) {
            return end(text, i);  // x^−2
        }
        if (Character.isDigit(first)) {
            while (i < text.length() && (Character.isDigit(text.charAt(i)) || text.charAt(i) == '.')) {
                i++;
            }
            return i;
        }
        if (Character.isLetter(first)) {
            while (i < text.length() && trails(text.charAt(i))) {
                i++;
            }
            return i;
        }
        return start;
    }

    // x₁, x̄
    private static boolean trails(char c) {
        int type = Character.getType(c);
        return c >= '₀' && c <= '₉' || type == Character.NON_SPACING_MARK || type == Character.ENCLOSING_MARK;
    }
}
