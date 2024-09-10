package engine.main;

import java.awt.*;

// grafische Darstellung von Text
public class Label extends GameObject {

    // darzustellender Text
    private String text;

    // Farbe des Textes
    private Color color;

    // Schriftart
    private Font font;

    @Override
    protected void load() {
        super.load();

        // Standardwerte
        text = "";
        color = Color.black;
        font = panel.getFont();
    }

    @Override
    void draw(Graphics2D g, int x, int y, int width, int height) {
        // zentriert Text in der Mitte des Objektes mit folgenden Berechnungen
        FontMetrics metrics = g.getFontMetrics(font);

        double f = Math.pow(panel.canvasSize.height / 500d, 0.95d);

        int textX  = x + width / 2 - (int) (metrics.stringWidth(text) * f / 2d);
        int textY = (int) (y + height / 2 - metrics.getHeight() / 2 + Math.pow(metrics.getAscent(), 0.9d) + panel.canvasSize.height / 85f);

        float fontSize = (float) (font.getSize() * f);

        // zeichnet Text
        g.setFont(font.deriveFont(fontSize));
        g.setColor(color);
        g.drawString(text, textX, textY);
        g.setFont(panel.getFont());
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Font getFont() {
        return font;
    }
}