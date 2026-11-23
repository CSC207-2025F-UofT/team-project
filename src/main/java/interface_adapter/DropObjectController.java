package interface_adapter;

import entity.ObjectPreset;
import use_case.DropObjectInputBoundary;
import use_case.DropObjectInputData;

public class DropObjectController {
    private final DropObjectInputBoundary dropObjectUseCase;

    public DropObjectController(DropObjectInputBoundary dropObjectUseCase) {
        this.dropObjectUseCase = dropObjectUseCase;
    }

    public void execute(ObjectPreset preset, double x, double y, double dropSpeed) {
        dropObjectUseCase.execute(new DropObjectInputData(dropSpeed, x, y, preset ));
    }
}
