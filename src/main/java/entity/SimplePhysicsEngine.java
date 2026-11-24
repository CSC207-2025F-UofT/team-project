package entity;

// Good-enough sphere physics on a heightmap (plan section 9).
// - Gravity always pulls down.
// - When a ball would end up under the ground, it is put back on top and the part of its
//   velocity going into the ground is removed, so it rolls along the slope.
//   Hitting hard enough flips that part instead, scaled by the preset's bounciness.
// - Friction slowly drains speed while touching the ground, so balls settle in valleys.
// - Ground that rises too steeply is a wall: the ball bounces back instead of climbing it.
// - Balls stop at the edge of the terrain.

public final class SimplePhysicsEngine implements PhysicsEngine {

    /** Longest step simulated at once, in seconds. Bigger dt values are split up. */
    static final double MAX_STEP = 1.0 / 120;

    /** Below this speed a landing ball stops bouncing and starts rolling, in m/s. */
    static final double MIN_BOUNCE_SPEED = 0.5;

    /** Ground rising more steeply than this (metres per metre) counts as a wall. */
    static final double MAX_CLIMB_SLOPE = 3.0;

    @Override
    public void step(Scene scene, double dt) {
        Terrain terrain = scene.getTerrain();
        if (terrain == null || dt <= 0) {
            return;
        }
        int steps = (int) Math.ceil(dt / MAX_STEP);
        double stepTime = dt / steps;
        for (int i = 0; i < steps; i++) {
            for (Ball ball : scene.getBalls()) {
                stepBall(ball, terrain, scene.getGravity(), stepTime);
            }
        }
    }

    private static void stepBall(Ball ball, Terrain terrain, double gravity, double dt) {
        ObjectPreset preset = ball.getPreset();
        double radius = preset.getRadius();
        Vector3D position = ball.getPosition();

        Vector3D velocity = ball.getVelocity().add(new Vector3D(0, 0, -gravity * dt));
        if (ball.isOnGround(terrain)) {
            velocity = velocity.scale(Math.exp(-preset.getFriction() * dt));
        }

        Vector3D next = position.add(velocity.scale(dt));

        // Stop at the edge: clamp inside and drop the speed pointing outward.
        double x = clamp(next.getX(), 0, terrain.width());
        double y = clamp(next.getY(), 0, terrain.depth());
        velocity = new Vector3D(x == next.getX() ? velocity.getX() : 0,
                y == next.getY() ? velocity.getY() : 0, velocity.getZ());
        next = new Vector3D(x, y, next.getZ());

        double ground = terrain.heightAt(next.getX(), next.getY());
        if (next.getZ() - radius < ground && isWall(terrain, position, next, ground)) {
            next = new Vector3D(position.getX(), position.getY(), next.getZ());
            double bounce = -preset.getBounciness();
            velocity = new Vector3D(velocity.getX() * bounce, velocity.getY() * bounce, velocity.getZ());
            ground = terrain.heightAt(next.getX(), next.getY());
        }

        if (next.getZ() - radius < ground) {
            next = new Vector3D(next.getX(), next.getY(), ground + radius);
            Vector3D normal = surfaceNormal(terrain, next.getX(), next.getY());
            double into = velocity.dot(normal);
            if (into < 0) {
                velocity = velocity.subtract(normal.scale(into));
                double bounceSpeed = -into * preset.getBounciness();
                if (bounceSpeed >= MIN_BOUNCE_SPEED) {
                    velocity = velocity.add(normal.scale(bounceSpeed));
                }
            }
        }

        ball.setPosition(next);
        ball.setVelocity(velocity);
    }

    private static boolean isWall(Terrain terrain, Vector3D from, Vector3D to, double groundAhead) {
        double run = Math.hypot(to.getX() - from.getX(), to.getY() - from.getY());
        double rise = groundAhead - terrain.heightAt(from.getX(), from.getY());
        return run > 0 && rise > MAX_CLIMB_SLOPE * run;
    }

    private static Vector3D surfaceNormal(Terrain terrain, double x, double y) {
        Vector3D slope = terrain.slopeAt(x, y);
        return new Vector3D(-slope.getX(), -slope.getY(), 1).normalize();
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}
