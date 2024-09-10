package engine.main;

import engine.tools.Settings;

import javax.swing.*;
import java.awt.*;

// Fenster indem das Spiel läuft
public class Window extends JFrame {

    // Component zur Anzeige der Inhalte
    // füllt das ganze Fenster aus
    final GamePanel gamePanel;

    // Fenster-Größe während das Fenster nicht im Fullscreen-Zustand ist
    private Dimension normalWindowSize;

    // Fenster-Position während das Fenster nicht im Fullscreen-Zustand ist
    private Point normalWindowLocation;

    public Window(Settings settings, GameObject environment, String iconSrc) {
        // legt Fenstertitel fest
        super("Die Legende von Müaluenie");

        gamePanel = new GamePanel(settings, environment, this);

        // lädt das App-Icon
        Image icon = new ImageIcon("assets\\" + iconSrc).getImage();

        // konfiguriert das Fenster
        setMinimumSize(new Dimension(settings.xTiles(), settings.yTiles()));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(icon);
        add(gamePanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        normalWindowSize = getSize();
        normalWindowLocation = getLocation();
    }

    void changeWindowState() {
        dispose();

        if (hasFullscreen()) {
            setUndecorated(false);
            setExtendedState(NORMAL);
            setSize(normalWindowSize);
            setLocation(normalWindowLocation);
        } else {
            normalWindowSize = getSize();
            normalWindowLocation = getLocation();
            setUndecorated(true);
            setExtendedState(MAXIMIZED_BOTH);
        }

        setVisible(true);
    }

    boolean hasFullscreen() {
        return getExtendedState() == MAXIMIZED_BOTH;
    }
}