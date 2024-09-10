package development.obstacles;

import engine.main.Collider;
import engine.main.ImageObject;

public class ObstacleGraves extends ImageObject {

int id = 0;
    protected void load() {
        super.load();
        //Zufälliges Festlegen Bild und Collider

        id = (int) (Math.random()* 2);

        if (id == 0) {
            setSrc("img\\obj\\obstacles\\graves-1.png");
            setGlobalPosition(getParent().getGlobalPosition().x + (float) Math.random() * (getParent().getWidth() - getWidth()), 4f);

            Collider collider = new Collider();
            addComponent(collider);
            collider.setPadding(6/32f, 13/32f, 4/32f, 0f);
        }
        if (id >= 1) {
            setSrc("img\\obj\\obstacles\\graves-2.png");
            setGlobalPosition(getParent().getGlobalPosition().x + (float) Math.random() * (getParent().getWidth() - getWidth()), 4f);

            Collider collider = new Collider();
            addComponent(collider);
            collider.setPadding(0f, 8/32f, 3/32f, 0f);
        }


    }
}
