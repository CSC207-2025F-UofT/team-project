package use_case.evaluate;

import entity.MathException;
import entity.MathLexer;

import java.util.List;

// same rule evaluate uses, just no saving
public class DefinitionCheckInteractor implements DefinitionCheckInputBoundary {
    @Override
    public boolean isDefinition(String latex) {
        if (latex == null) {
            return false;
        }
        try {
            List<String> tokens = MathLexer.tokenize(latex);
            if (!tokens.isEmpty() && tokens.get(tokens.size() - 1).equals("=")) {
                tokens.remove(tokens.size() - 1);  // f(x)= drops to f(x), no body = no
            }
            return FunctionDefiner.isDefinition(tokens);
        } catch (MathException e) {
            return false;
        }
    }
}
