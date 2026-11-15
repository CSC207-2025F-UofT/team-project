package use_case.evaluate;

// is this line a f(x)=... w/ a body? canvas saves those w/o a click
public interface DefinitionCheckInputBoundary {
    boolean isDefinition(String latex);
}
