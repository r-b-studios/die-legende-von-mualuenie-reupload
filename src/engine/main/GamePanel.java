package engine.main;

import development.data.DataFile;
import engine.tools.Point;
import engine.tools.SafeList;
import engine.tools.Settings;
import engine.tools.Size;

import javax.swing.*;
import java.awt.*;

// Panel auf dem die Spielelemente gezeichnet werden
public class GamePanel extends JPanel implements Runnable {

    // enthält alle anzuzeigenden und zu updatenden GameObjects
    final SafeList<GameObject> gameObjects = new SafeList<>();

    // Input-Event-Handler zum Lauschen auf Maus- und Tastatureingabeereignisse
    InputHandler input = new InputHandler();

    // Größe der Fläche, auf der das Spiel abgebildet wird
    // wird in paintComponent() angepasst
    Size canvasSize = Size.ZERO;

    // Einstellungen für die Koordinateneinheiten und Größen
    final Settings settings;

    // Referenz zum Fenster
    final Window window;

    // Frames pro Sekunde
    float fps;

    // das Objekt, welches alle anderen Objekte enthält
    GameObject environment;

    // letzter Frame in Nanosekunden
    private long lastFrame;

    // FPS-Anzeige-Text (wird links oben angezeigt)
    private String fpsDisplay;

    public GamePanel(Settings settings, GameObject environment, Window window) {

        // legt Attribute Fest
        this.settings = settings;
        this.environment = environment;
        this.window = window;
        this.lastFrame = System.nanoTime();

        // bestimmt Eigenschaften dieses JPanels
        setBackground(Color.black);
        setPreferredSize(new Dimension(800, 500));
        setDoubleBuffered(true);
        setFocusable(true);
        addKeyListener(input);
        addMouseListener(input);

        // startet Threads

        // loopThread: wird jeden Frame ausgeführt
        // um Spielelemente jeden Frame zu zeichnen und zu aktualisieren
        Thread loopThread = new Thread(this);
        loopThread.start();

        // fpsThread: wird jede Sekunde ausgeführt
        // Zum Anzeigen des fpsDisplay String links oben
        Thread fpsThread = new Thread(this::configFPSDisplay);
        fpsThread.start();

        // lädt enivronment und fügt es zu gameObjects
        environment.panel = this;
        environment.load();
        gameObjects.add(environment);
    }

    // Haupt-Thread
    @Override
    public void run() {
        try {
            Thread.sleep(1000);

            // Endlos-Schleife
            while (true) {
                update();

                // repaint, damit paintComponent in diesem Frame aufgerufen wird
                repaint();

                Thread.sleep((int) (1000f / DataFile.getMaxFPS()));
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    // Konfiguriert jede Sekunde die FPS-Anzeige
    private void configFPSDisplay() {
        while (true) {
            try {
                // gibt
                fpsDisplay = Math.round(fps) + "/" + DataFile.getMaxFPS() + " FPS";
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    // wird jeden Frame ausgeführt
    private void update() {

        // FPS berechnen
        fps = 1_000_000_000f / (System.nanoTime() - lastFrame);
        lastFrame = System.nanoTime();

        // Bubble-Sort nach Attribut GameObject.layer
        for (int i = 0; i < gameObjects.size(); i++) {
            for (int j = 0; j < gameObjects.size() - 1; j++) {
                if (gameObjects.get(j).getLayer() > gameObjects.get(j + 1).getLayer()) {
                    GameObject temp = gameObjects.get(j);
                    gameObjects.set(j, gameObjects.get(j + 1));
                    gameObjects.set(j + 1, temp);
                }
            }
        }

        // Führt für jedes aktivierte GameObject update() aus
        gameObjects.forEach(obj -> {
            if (obj.getActive()) {
                obj.update();
            }
        });

        // updatet den InputHandler
        input.update();
    }

    // Methode zum Zeichnen von grafischen Elementen
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Seitenverhältnisse von Fenster und Spielfeld
        float canvasRatio = (float) settings.xTiles() / settings.yTiles();
        float windowRatio = (float) getWidth() / getHeight();

        g2d.setColor(Color.black);

        // berechnete im Folgenden die Position und Größe der schwarzen Fensterränder,
        // die dafür sorgen, dass das Spielfeld immer gleich groß ist
        Point origin, end;
        int width, height, borderSize;

        Point space1Pos, space2Pos;
        Size space1Size, space2Size;

        if (windowRatio > canvasRatio) {
            borderSize = (getWidth() - (int) (getHeight() * canvasRatio)) / 2;

            origin = new Point(borderSize, 0);
            end = new Point(getWidth() - borderSize, 0);

            canvasSize = new Size(getWidth() - 2 * (int) origin.x, getHeight());
        } else {
            borderSize = (getHeight() - (int) (getWidth() / canvasRatio)) / 2;

            origin = new Point(0, borderSize);
            end = new Point(0, getHeight() - borderSize);

            canvasSize = new Size(getWidth(), getHeight() - 2 * (int) origin.y);
        }

        // durchläuft jedes GameObject
        gameObjects.forEach(obj -> {
            // rechnet die Werte von den GameObjects wieder in Pixel-Einheit um
            int x = (int) (origin.x + obj.getGlobalPosition().x / settings.xTiles() * canvasSize.width);
            int y = (int) (origin.y + obj.getGlobalPosition().y / settings.yTiles() * canvasSize.height);
            int w = (int) (obj.getSize().width / settings.xTiles() * canvasSize.width);
            int h = (int) (obj.getSize().height / settings.yTiles() * canvasSize.height);

            obj.origin = origin;
            obj.draw(g2d, x, y, w, h);
        });

        g2d.setColor(Color.black);

        // zeichnet schwarzen Rand über die anderen GameObjects
        if (windowRatio > canvasRatio) {
            g2d.fillRect(0, 0, borderSize, getHeight());
            g2d.fillRect(getWidth() - borderSize, 0, borderSize, getHeight());
        } else {
            g2d.fillRect(0, 0, getWidth(), borderSize);
            g2d.fillRect(0, getHeight() - borderSize, getWidth(), borderSize);
        }

        // Zeichnet FPS-Anzeige
        g2d.setColor(Color.gray);
        g2d.drawString(fpsDisplay, 0, 10);
    }
}