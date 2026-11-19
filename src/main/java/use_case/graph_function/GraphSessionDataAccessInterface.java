package use_case.graph_function;

import entity.graph.Plot;

// keeps the graph that's on screen so pan / zoom / trace know what they're moving
public interface GraphSessionDataAccessInterface {
    void save(Plot plot);

    // null before anything was graphed
    Plot current();
}
