package engine.main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// Darstellung von Grafiken
public class ImageObject extends GameObject {

    // Bilddatei
    BufferedImage img;

    // lädt die Grafik mit Dateipfad
    public void setSrc(String src) {
        if (src != null) {
            File file = new File("assets\\" + src);

            if (file.exists()) {
                try {
                    img = ImageIO.read(file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    void draw(Graphics2D g, int x, int y, int width, int height) {
        if (img != null) {
            // zeichnet Grafik
            g.drawImage(img, x, y, width, height, null);
        }
    }
}