package entity;

import entity.TerrainFeature.Kind;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimplePhysicsEngineTest {

    private static final double DELTA = 1e-6;
    private static final double TICK = 1.0 / 60;

    private final PhysicsEngine engine = new SimplePhysicsEngine();

    private static ObjectPreset preset(double bounciness, double friction) {
        return new ObjectPreset("Test ball", 0.5, 1, bounciness, friction, 0xffffff);
    }

    private static Scene sceneWith(Terrain terrain, Ball ball) {
        Scene scene = new Scene(terrain);
        scene.addBall(ball);
        return scene;
    }

    private static Terrain flat() {
        return new TerrainBuilder("Flat").gridSize(20, 20).build();
    }

    private void run(Scene scene, double seconds) {
        for (double time = 0; time < seconds; time += TICK) {
            engine.step(scene, TICK);
        }
    }

    @Test
    void ballInTheAirFalls() {
        Ball ball = new Ball(new Vector3D(10, 10, 20), Vector3D.ZERO, preset(0, 0));
        engine.step(sceneWith(flat(), ball), 0.1);
        assertTrue(ball.getPosition().getZ() < 20);
        assertEquals(-0.981, ball.getVelocity().getZ(), 1e-3);
    }

    @Test
    void ballRestingOnFlatGroundStaysPut() {
        Ball ball = new Ball(new Vector3D(10, 10, 0.5), Vector3D.ZERO, preset(0.5, 0.4));
        run(sceneWith(flat(), ball), 2);
        assertEquals(10, ball.getPosition().getX(), DELTA);
        assertEquals(10, ball.getPosition().getY(), DELTA);
        assertEquals(0.5, ball.getPosition().getZ(), DELTA);
        assertTrue(ball.isOnGround(flat()));
    }

    @Test
    void ballRollsDownhill() {
        Terrain ramp = new TerrainBuilder("Ramp").gridSize(20, 20)
                .addFeature(new TerrainFeature(Kind.RAMP, 10, 10, 16, 8, "EAST"))
                .build();
        double startX = 20;
        Ball ball = new Ball(new Vector3D(startX, 20, ramp.heightAt(startX, 20) + 0.5),
                Vector3D.ZERO, preset(0, 0.1));
        run(sceneWith(ramp, ball), 1);
        assertTrue(ball.getPosition().getX() < startX, "ball should roll toward lower x");
        assertTrue(ball.getVelocity().getX() < 0);
    }

    @Test
    void ballSettlesAtBottomOfValley() {
        Terrain valley = new TerrainBuilder("Valley").gridSize(20, 20).ground(10)
                .addFeature(new TerrainFeature(Kind.VALLEY, 10, 10, 8, 6, null))
                .build();
        Ball ball = new Ball(new Vector3D(26, 20, 20), Vector3D.ZERO, preset(0.3, 0.8));
        run(sceneWith(valley, ball), 60);
        assertEquals(20, ball.getPosition().getX(), 1.0);
        assertEquals(20, ball.getPosition().getY(), 1.0);
        assertTrue(ball.getVelocity().length() < 0.1, "ball should have nearly stopped");
    }

    @Test
    void ballNeverEndsUpUnderTheGround() {
        Terrain hills = new TerrainBuilder("Hills").gridSize(20, 20)
                .addFeature(new TerrainFeature(Kind.HILL, 6, 6, 6, 10, null))
                .addFeature(new TerrainFeature(Kind.VALLEY, 13, 12, 5, 6, null))
                .build();
        Ball ball = new Ball(new Vector3D(12, 12, 30), new Vector3D(4, 3, -5), preset(0.7, 0.2));
        Scene scene = sceneWith(hills, ball);
        for (int tick = 0; tick < 600; tick++) {
            engine.step(scene, TICK);
            Vector3D p = ball.getPosition();
            assertTrue(p.getZ() - 0.5 >= hills.heightAt(p.getX(), p.getY()) - DELTA);
        }
    }

    @Test
    void bouncyBallBouncesWhenItLands() {
        Ball ball = new Ball(new Vector3D(10, 10, 5), Vector3D.ZERO, preset(0.8, 0));
        Scene scene = sceneWith(flat(), ball);
        boolean wentUp = false;
        for (int tick = 0; tick < 120 && !wentUp; tick++) {
            engine.step(scene, TICK);
            wentUp = ball.getVelocity().getZ() > 0;
        }
        assertTrue(wentUp);
    }

    @Test
    void deadBallDoesNotBounce() {
        Ball ball = new Ball(new Vector3D(10, 10, 5), Vector3D.ZERO, preset(0, 0));
        Scene scene = sceneWith(flat(), ball);
        for (int tick = 0; tick < 120; tick++) {
            engine.step(scene, TICK);
            assertTrue(ball.getVelocity().getZ() <= 0);
        }
        assertEquals(0.5, ball.getPosition().getZ(), DELTA);
    }

    @Test
    void ballStopsAtTheEdge() {
        Terrain terrain = flat();
        Ball ball = new Ball(new Vector3D(terrain.width() - 1, 10, 0.5), new Vector3D(10, 0, 0), preset(0, 0));
        run(sceneWith(terrain, ball), 1);
        assertEquals(terrain.width(), ball.getPosition().getX(), DELTA);
        assertEquals(0, ball.getVelocity().getX(), DELTA);
    }

    @Test
    void ballBouncesOffBlockInsteadOfClimbingIt() {
        Terrain city = new TerrainBuilder("City").gridSize(20, 20)
                .addFeature(new TerrainFeature(Kind.BLOCK, 14, 10, 4, 25, null))
                .build();
        Ball ball = new Ball(new Vector3D(10, 20, 0.5), new Vector3D(6, 0, 0), preset(0.5, 0));
        run(sceneWith(city, ball), 2);
        assertTrue(ball.getPosition().getZ() < 1, "ball should stay on the street");
        assertTrue(ball.getVelocity().getX() < 0, "ball should be heading back");
    }

    @Test
    void doesNothingWithoutTerrainOrTime() {
        Ball ball = new Ball(new Vector3D(1, 2, 3), Vector3D.ZERO, preset(0, 0));
        Scene empty = new Scene();
        empty.addBall(ball);
        engine.step(empty, 1);
        engine.step(sceneWith(flat(), ball), 0);
        assertEquals(3, ball.getPosition().getZ(), DELTA);
    }
}
