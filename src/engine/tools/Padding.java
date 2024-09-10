package engine.tools;

// Verkleinerung der Ränder eines Objektes
public record Padding(float left, float top, float right, float bottom) {

    // keine Verkleinerung
    public static final Padding ZERO = new Padding(0, 0, 0, 0);
}
