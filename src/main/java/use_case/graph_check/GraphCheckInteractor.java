package use_case.graph_check;

import entity.LatexFunctionParser;

import java.util.regex.Pattern;

// graphable = y / f(x) on either side of the = and the graph parser takes it
public class GraphCheckInteractor implements GraphCheckInputBoundary {
    private static final String NAME = "(y|[a-zA-Z]\\s*\\(\\s*x\\s*\\))";
    private static final Pattern FUNCTION_LEFT = Pattern.compile("^\\s*" + NAME + "\\s*=.*", Pattern.DOTALL);
    // x^2 = y, same thing written the other way round
    private static final Pattern FUNCTION_RIGHT = Pattern.compile(".*=\\s*" + NAME + "\\s*$", Pattern.DOTALL);

    private final LatexFunctionParser parser;

    public GraphCheckInteractor(LatexFunctionParser parser) {
        this.parser = parser;
    }

    @Override
    public boolean canGraph(String latex) {
        if (latex == null || !namesTheFunction(latex)) {
            return false;
        }
        try {
            parser.parse(latex);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean namesTheFunction(String latex) {
        return FUNCTION_LEFT.matcher(latex).matches() || FUNCTION_RIGHT.matcher(latex).matches();
    }
}
