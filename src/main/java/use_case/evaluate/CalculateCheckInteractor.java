package use_case.evaluate;

import entity.MathLexer;
import entity.MathParser;
import entity.node.Node;
import entity.node.RelationNode;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

// same routing evaluate does, just no working it out. f(x)=... is no, it saves itself
public class CalculateCheckInteractor implements CalculateCheckInputBoundary {
    private final FunctionDataAccessInterface functionDataAccess;

    public CalculateCheckInteractor(FunctionDataAccessInterface functionDataAccess) {
        this.functionDataAccess = functionDataAccess;
    }

    @Override
    public boolean canCalculate(String latex) {
        if (latex == null) {
            return false;
        }
        try {
            List<String> tokens = MathLexer.tokenize(latex);
            if (!tokens.isEmpty() && tokens.get(tokens.size() - 1).equals("=")) {
                tokens.remove(tokens.size() - 1);
            }
            if (tokens.isEmpty() || FunctionDefiner.isDefinition(tokens)
                    || tokens.get(0).equals("=") || tokens.get(tokens.size() - 1).equals("=")) {
                return false;
            }
            Node node = new MathParser(tokens, functionDataAccess.getAllFunctions()).parse();
            Set<String> unknowns = new LinkedHashSet<>();
            node.collectFreeVariables(unknowns);
            if (unknowns.isEmpty()) {
                return true;
            }
            if (manyEquals(node)) {
                return false;
            }
            if (!node.isCondition()) {
                return node.containsDerivative();
            }
            return unknowns.size() == 1;
        } catch (RuntimeException | StackOverflowError e) {
            return false;
        }
    }

    private static boolean manyEquals(Node node) {
        return node instanceof RelationNode
                && Collections.frequency(((RelationNode) node).getOperators(), "=") > 1;
    }
}
