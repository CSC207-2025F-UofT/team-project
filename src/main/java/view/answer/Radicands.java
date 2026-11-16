package view.answer;

import java.util.ArrayList;
import java.util.List;

// what each √ in an answer covers: one symbol or a (...) group, same as ExprWriter writes them
final class Radicands {
    // radicand is text[sign + 1, end)
    static final class Root {
        final int sign;
        final int end;

        Root(int sign, int end) {
            this.sign = sign;
            this.end = end;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Root && ((Root) o).sign == sign && ((Root) o).end == end;
        }

        @Override
        public int hashCode() {
            return 31 * sign + end;
        }

        @Override
        public String toString() {
            return "√[" + sign + "," + end + ")";
        }
    }

    private Radicands() {
    }

    static List<Root> find(String text) {
        List<Root> roots = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '√') {
                int end = end(text, i + 1);
                if (end > i + 1) {
                    roots.add(new Root(i, end));
                }
            }
        }
        return roots;
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
