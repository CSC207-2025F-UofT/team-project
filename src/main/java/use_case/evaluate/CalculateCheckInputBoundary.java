package use_case.evaluate;

// would Calculate get an answer out of this, w/o actually running it
public interface CalculateCheckInputBoundary {
    boolean canCalculate(String latex);
}
