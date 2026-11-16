package entity;

// An immutable 3D vector.
// x and y run across the terrain grid, z is height.
// Every operation returns a new vector.

public final class Vector3D {

    public static final Vector3D ZERO = new Vector3D(0, 0, 0);

    private final double x;
    private final double y;
    private final double z;

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Vector3D add(Vector3D other) {
        return new Vector3D(x + other.x, y + other.y, z + other.z);
    }

    public Vector3D subtract(Vector3D other) {
        return new Vector3D(x - other.x, y - other.y, z - other.z);
    }

    public Vector3D scale(double factor) {
        return new Vector3D(x * factor, y * factor, z * factor);
    }

    public double dot(Vector3D other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public double length() {
        return Math.sqrt(dot(this));
    }

    // Returns a vector with the same direction and length 1.
    // The zero vector has no direction, so it returns ZERO instead of dividing by zero.

    public Vector3D normalize() {
        double length = length();
        if (length == 0) {
            return ZERO;
        }
        return scale(1 / length);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
