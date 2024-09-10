package development.world;

import engine.main.Collider;
import engine.main.ImageObject;

public class Platform extends ImageObject {

    float time = 0;
    boolean floatingUp = true;

    boolean randomMovement = false;

    @Override
    protected void load() {
        super.load();
        setSrc("img\\obj\\world\\dark-chunk\\dark-platform-1.png");
        if (Math.random() > .7) {
            randomMovement = true;
            setSrc("img\\obj\\world\\dark-chunk\\dark-platform-2.png");
        }
        setGlobalPosition(12.5f, 5f);
        setSize(2f, 1f);
        Collider collider = new Collider();
        addComponent(collider);
        collider.setPadding(0f,8f/32f,0f,15f/32f);
    }

    @Override
    protected void update() {
        super.update();

        //bewegung auf und ab
        if (!randomMovement) {
            if (time >= 2 && floatingUp) {
                floatingUp = false;
                time = 0;
            }
            if (time >= 2 && !floatingUp) {
                floatingUp = true;
                time = 0;
            }
        } else {
            if (time * Math.random() >= 1 && floatingUp) {
                floatingUp = false;
                time = 0;
            }
            if (time * Math.random() >= 1 && !floatingUp) {
                floatingUp = true;
                time = 0;
            }
        }

        if (floatingUp) move(0, -1f / getFPS());
        if (!floatingUp) move(0, 1f / getFPS());

        time += 1 / getFPS();
    }
}

