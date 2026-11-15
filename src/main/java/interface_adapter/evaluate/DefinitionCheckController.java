package interface_adapter.evaluate;

import use_case.evaluate.DefinitionCheckInputBoundary;

public class DefinitionCheckController {
    private final DefinitionCheckInputBoundary interactor;

    public DefinitionCheckController(DefinitionCheckInputBoundary interactor) {
        this.interactor = interactor;
    }

    public boolean isDefinition(String latex) {
        return interactor.isDefinition(latex);
    }
}
