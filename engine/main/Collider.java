package engine.main;

import engine.enums.Collision;
import engine.events.BiEvent;
import engine.tools.Padding;
import engine.tools.Point;
import engine.tools.SafeList;
import engine.tools.Size;

// zum Überprüfen ob Objekt zusammenstoßen
public class Collider extends Component{

    private Padding padding = Padding.ZERO;

    // Zusammenstoß-Ereignis
    public final BiEvent<Collider, Collision> collide = new BiEvent<>();

    // alle Objekte deren Collider gerade mit diesem Collider zusammenstößt
    private final SafeList<GameObject> collidingObjects = new SafeList<>();

    @Override
    void update() {
        SafeList<GameObject> list = new SafeList<>();
        list.addAll(owner.panel.gameObjects);

        collidingObjects.clear();

        list.forEach(obj -> {
            if (obj != owner) {
                obj.getComponents(Collider.class).forEach(c2 -> {
                    // prüft auf Kollision
                    if (
                        getPosition().x <= c2.getPosition().x + c2.getSize().width &&
                        getPosition().x + getSize().width >= c2.getPosition().x &&
                        getPosition().y <= c2.getPosition().y + c2.getSize().height &&
                        getPosition().y + getSize().height >= c2.getPosition().y
                    ) {
                        var hDis = getPosition().x + getSize().width - c2.getPosition().x;
                        var vDis = getPosition().y + getSize().height - c2.getPosition().y;

                        // berechnet ob die Collider vertikal oder horizontal zusammenstoßen
                        var type = hDis > vDis && hDis >= 0 ? Collision.VERTICAL : Collision.HORIZONTAL;

                        collide.invoke(c2, type);
                        collidingObjects.add(obj);
                    }
                });
            }
        });
    }

    private Point getPosition() {
        return new Point(
            owner.getGlobalPosition().x + padding.left(),
            owner.getGlobalPosition().y + padding.top()
        );
    }

    private Size getSize() {
        return new Size(
            owner.getSize().width - padding.left() - padding.right(),
            owner.getSize().height - padding.top() - padding.bottom()
        );
    }

    public void setPadding(float left, float top, float right, float bottom) {
        setPadding(new Padding(left, top, right, bottom));
    }

    public void setPadding(Padding padding) {
        this.padding = padding;
    }

    public Padding getPadding() {
        return padding;
    }

    public boolean getCollision(GameObject... others) {
        if (collidingObjects.size() > 0) {
            if (others.length > 0) {
                for (GameObject obj : others) {
                    if (collidingObjects.contains(obj)) {
                        return true;
                    }
                }

                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}