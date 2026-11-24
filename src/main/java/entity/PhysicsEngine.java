package entity;

// Moves every ball in a scene forward in time.
// Implementations can be swapped without touching the use cases (Strategy pattern).

public interface PhysicsEngine {

    /** Advances every ball in the scene by dt seconds of sim time. */
    void step(Scene scene, double dt);
}
