package use_case.evaluate;

import entity.MathParser;

import java.util.List;

// how the written formula ends/looks, presenter turns this into a canvas spot
public enum FormulaShape {
    ENDS_WITH_EQUALS, RELATION, PLAIN;

    static FormulaShape of(List<String> tokens) {
        if (!tokens.isEmpty() && "=".equals(tokens.get(tokens.size() - 1))) {
            return ENDS_WITH_EQUALS;
        }
        int depth = 0;
        for (String t : tokens) {
            depth += "{".equals(t) ? 1 : "}".equals(t) ? -1 : 0;
            if (depth == 0 && MathParser.isRelation(t)) {
                return RELATION;  // skips the i=1 under a sum
            }
        }
        return PLAIN;
    }
}
