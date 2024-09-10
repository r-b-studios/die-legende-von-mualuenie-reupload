package engine.tools;

// Position im Koordinatensystem
public class Point {

    // Ursprung
    public static Point ZERO = new Point(0, 0);

    // x- und y-Wert
    public float x, y;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }

    // Konvertierung zu Text, zum Debugging
    @Override
    public String toString() {
        return "Point{" + "x=" + x + ", y=" + y + '}';
    }
}