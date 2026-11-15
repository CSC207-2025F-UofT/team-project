package entity;

import java.util.List;

public class MathSyntaxException extends MathException {
    private final SyntaxProblem problem;  // never null
    private final List<String> details;   // raw tokens/names, in the order the words need them

    public MathSyntaxException(SyntaxProblem problem, String... details) {
        super(problem + (details.length == 0 ? "" : " " + List.of(details)));
        this.problem = problem;
        this.details = List.of(details);
    }

    public SyntaxProblem getProblem() {
        return problem;
    }

    public List<String> getDetails() {
        return details;
    }
}
