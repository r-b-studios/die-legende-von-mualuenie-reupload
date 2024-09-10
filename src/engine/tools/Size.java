package engine.tools;

// Größe eines Objektes
public class Size {

    // keine Größe
    public static Size ZERO = new Size(0, 0);
    // eine Einheit in x- und y-Richtung
    public static Size ONE = new Size(1, 1);

    // Breite
    public float width;

    // Höhe
    public float height;

    public Size(float width, float height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "Size{" + "width=" + width + ", height=" + height + '}';
    }
}