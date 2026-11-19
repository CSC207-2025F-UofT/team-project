package use_case.evaluate;

import entity.MathLexer;
import entity.MathParser;

import java.util.List;

// top-level relation w/ stuff on both sides = done writing. 2x>5 yes, 2x> / sum_{n=0} no
public class QuestionCheckInteractor implements QuestionCheckInputBoundary {
    @Override
    public boolean isQuestion(String latex) {
        if (latex == null) {
            return false;
        }
        try {
            List<String> tokens = MathLexer.tokenize(latex);
            return FormulaShape.of(tokens) == FormulaShape.RELATION
                    && !MathParser.isRelation(tokens.get(0))
                    && !MathParser.isRelation(tokens.get(tokens.size() - 1));
        } catch (RuntimeException e) {
            return false;
        }
    }
}
