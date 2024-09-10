package engine.main;

import java.awt.*;

// stellt ein Rechteck dar
public class RectObject extends GameObject {

    // Farbe des Rechteckes
    private Color color = Color.black;

    // zeichnet das Rechteck
    @Override
    void draw(Graphics2D g, int x, int y, int width, int height) {
        g.setColor(color);
        g.fillRect(x, y, width, height);
    }

    public void setColor(Color color) {
        this.color = color;
    }
}