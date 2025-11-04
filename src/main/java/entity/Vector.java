package entity;

//Simple vector class with basic vector operations.
public class Vector{
    public double x;
    public double y;

    public Vector(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void add(Vector vec){
        this.x += vec.x;
        this.y += vec.y;
    }

    public void sub(Vector vec){
        this.x -= vec.x;
        this.y -= vec.y;
    }

    public void scale(double s){
        this.x *= s;
        this.y *= s;
    }
}