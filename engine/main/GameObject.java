package engine.main;

import engine.tools.Point;
import engine.tools.SafeList;
import engine.tools.Size;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

// Superklasse für alle Objekte auf der Spieloberfläche
public class GameObject {

    // Position im Spielfenster
    private Point position;

    // anzuzeigende Größe
    private Size size;

    // GameObject zu dem dieses Objekt sich relativ bewegt
    private GameObject parent;

    // Position in z-Achse
    private int layer;

    // Referenz auf Game-Panel
    GamePanel panel;

    // Ursprungs-Position des Koordinatensystems
    Point origin;

    // Objekte, die sich relativ zu diesem Objekt bewegen
    private SafeList<GameObject> children;

    // Komponenten, die diesem Objekt weitere Funktionen verleihen
    private SafeList<Component> components;

    // Objekt wird nicht mehr geupdated
    private boolean active;


    // wird nach das Objekt im Spielfeld hinzugefügt wurde aufgerufen
    // soll den Konstruktor ersetzen, damit zu dem Zeitpunkt die meisten Werte nicht mehr null sind
    protected void load() {
        // setzt Attribute auf Standard-Werte
        position = Point.ZERO;
        size = Size.ONE;
        children = new SafeList<>();
        components = new SafeList<>();
        active = true;
    }

    // wird jeden Frame aufgerufen
    protected void update() {
        components.forEach(Component::update);
    }

    // zum Zeichnen grafischer Elemente
    void draw(Graphics2D g, int x, int y, int width, int height) { }

    // löscht dieses Objekt aus dem Spielfeld
    public void destroy() {
        destroy(this);
    }

    // löscht mehere Objekte aus dem Spielfeld
    protected void destroy(GameObject... objects) {
        for (GameObject obj : objects) {
            destroy(obj.children.toArray(new GameObject[0]));
            panel.gameObjects.remove(obj);
        }
    }

    // löscht dieses Objekt und fügt ein anderes dem gleichen Parent hinzu
    public void replace(GameObject other) {
        getParent().add(other);
        destroy();
    }

    // gibt die Frames pPro Sekunde zurück
    protected float getFPS() {
        return panel.fps;
    }

    // gibt eine Klasse mit Eingabe-Events zurück
    protected InputHandler getInput() {
        return panel.input;
    }

    public void setPosition(float x, float y) {
        setPosition(new Point(x, y));
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setX(float x) {
        position.x = x;
    }

    public void setY(float y) {
        position.y = y;
    }

    public Point getPosition() {
        return position;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }

    // Position relativ zum Ursprung
    public Point getGlobalPosition() {
        if (parent == null) {
            return getPosition();
        } else {
            return new Point(getPosition().x + parent.getGlobalPosition().x, getPosition().y + parent.getGlobalPosition().y);
        }
    }

    // setzt Position relativ zum Ursprung
    public void setGlobalPosition(float x, float y) {
        Point pos = new Point(x - parent.getGlobalPosition().x, y - parent.getGlobalPosition().y);
        setPosition(pos);
    }

    // setzt Position relativ zum Ursprung
    public void setGlobalPosition(Point position) {
        setGlobalPosition(position.x, position.y);
    }

    public void setSize(float width, float height) {
        setSize(new Size(width, height));
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public void setWidth(float width) {
        size.width = width;
    }

    public void setHeight(float height) {
        size.height = height;
    }

    public Size getSize() {
        return size;
    }

    public float getWidth() {
        return size.width;
    }

    public float getHeight() {
        return size.height;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getLayer() {
        return layer;
    }

    public void setParent(GameObject parent) {
        this.parent = parent;
        setGlobalPosition(getGlobalPosition());
    }

    public GameObject getParent() {
        return parent;
    }

    public SafeList<GameObject> getChildren() {
        return (SafeList<GameObject>) children.clone();
    }

    // bewegt das Objekt
    public void move(float x, float y) {
        setPosition(getPosition().x + x, getPosition().y + y);
    }

    // (de)aktiviert dieses Objekt und alle Kinder
    public void setActive(boolean active) {
        this.active = active;
        children.forEach(c -> c.setActive(active));
    }

    public boolean getActive() {
        return active;
    }


    // fügt GameObjetcts zum Environment-Objekt hinzu
    protected void add(GameObject... objects) {
        panel.environment.addChildren(objects);
    }

    // fügt GameObjetcts als Children dieses Objekts hinzu
    public void addChildren(GameObject... objects) {
        for (GameObject obj : objects) {
            children.add(obj);
            obj.parent = this;
            obj.panel = panel;
            panel.gameObjects.add(obj);
            obj.load();
        }
    }

    // fügt diesem Objekt einen Komponente hinzu
    public void addComponent(Component component) {
        component.owner = this;
        components.add(component);
    }

    // entfernt von diesem Objekt einen Komponente
    public void removeComponent(Component component) {
        components.remove(component);
    }

    // entfernt von diesem Objekt Komponenten mit einem bestimmten Typ
    public void removeComponents(Class<? super Component> type) {
        components.forEach(c -> {
            if (type.isInstance(c)) {
                components.remove(c);
            }
        });
    }

    // Komponenten von diesem Objekt mit einem bestimmten Typ
    public <T extends Component> SafeList<T> getComponents(Class<T> type) {
        SafeList<T> list = new SafeList<>();

        components.forEach(c -> {
            if (type.isInstance(c)) {
                list.add((T) c);
            }
        });

        return list;
    }

    // Spielfeld-Größe
    public Size getCanvasSize() {
        return new Size(panel.settings.xTiles(), panel.settings.yTiles());
    }

    // Setzt den Hintergrund vom Spielfeld
    protected void setCanvasBackground(Color bgColor) {
        panel.setBackground(bgColor);
    }

    // Cursor-Position
    protected Point getCursorPosition() {
        if (origin == null) {
            return Point.ZERO;
        } else {
            var pos = MouseInfo.getPointerInfo().getLocation();

            // wandelt Werte entsprechend der in den Settings angegebenen Einheiten um
            float x = (pos.x - origin.x - panel.window.getX()) * panel.settings.xTiles() / (panel.getWidth() - 2 * origin.x) - .1f;
            float y = (pos.y - origin.y - panel.window.getY() - panel.window.getInsets().top) * panel.settings.yTiles() / (panel.getHeight() - 2 * origin.y);

            return new Point(x, y);
        }
    }

    protected static Font getFont(String src) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font font = null;

        try {
            File file = new File("assets\\" + src);
            font = Font.createFont(Font.TRUETYPE_FONT, file);

            if (!Arrays.asList(ge.getAllFonts()).contains(font)) {
                ge.registerFont(font);
            }
        } catch (IOException | FontFormatException ex) {
            ex.printStackTrace();
        }

        return font.deriveFont(12.5f);
    }

    protected void changeWindowState() {
        panel.window.changeWindowState();
    }

    protected boolean hasFullscreen() {
        return panel.window.hasFullscreen();
    }
}