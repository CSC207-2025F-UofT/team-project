package data_access;

import entity.graph.Plot;
import use_case.graph_function.GraphSessionDataAccessInterface;

// the graph that's up right now, graphing runs off the ui thread so it's locked
public class InMemoryGraphSessionDataAccessObject implements GraphSessionDataAccessInterface {
    private Plot current;

    @Override
    public synchronized void save(Plot plot) {
        current = plot;
    }

    @Override
    public synchronized Plot current() {
        return current;
    }
}
