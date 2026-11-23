package entity;

public class TerrainFeature {

    public enum Kind { HILL, VALLEY, RAMP, BLOCK }

    private final Kind kind;
    private double x;
    private double y;
    private double size;
    private double height;
    private String direction;

    public TerrainFeature(Kind kind, double x, double y, double size, double height, String direction) {
        this.kind = kind;
        this.x = x;
        this.y = y;
        this.size = size;
        this.height = height;
        this.direction = direction;
    }

    public Kind getKind() {
        return kind;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSize() {
        return size;
    }

    public double getHeight() {
        return height;
    }

    public String getDirection() {
        return direction;
    }

    public double heightAt(double px, double py) {
        double dx = px - x;
        double dy = py - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        switch (kind) {
            case HILL:
                if (distance >= size) {
                    return 0;
                }
                return height * (1 - distance / size);
            case VALLEY:
                if (distance >= size) {
                    return 0;
                }
                return -height * (1 - distance / size);
            case BLOCK:
                if (Math.abs(dx) > size / 2 || Math.abs(dy) > size / 2) {
                    return 0;
                }
                return height;
            case RAMP:
                if (Math.abs(dx) > size / 2 || Math.abs(dy) > size / 2) {
                    return 0;
                }
                double along;
                switch (direction) {
                    case "NORTH":
                        along = -dy;
                        break;
                    case "SOUTH":
                        along = dy;
                        break;
                    case "EAST":
                        along = dx;
                        break;
                    case "WEST":
                        along = -dx;
                        break;
                    default:
                        return 0;
                }
                return height * (along + size / 2) / size;
            default:
                return 0;
        }
    }


}
