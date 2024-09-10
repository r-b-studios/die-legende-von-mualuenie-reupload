package development.obstacles;

import development.world.PlayMode;
import development.enums.Scene;
import engine.main.Animator;
import engine.main.Collider;
import engine.main.ImageObject;
import engine.enums.Collision;
import engine.tools.AnimationFrame;

public class Obstacle extends ImageObject {

    int id = 0;

    private  final AnimationFrame[] campfire = new AnimationFrame[]{
            new AnimationFrame(.2f, () -> setSrc("img\\obj\\obstacles\\campfire-1.png")),
            new AnimationFrame(.2f, () -> setSrc("img\\obj\\obstacles\\campfire-2.png")),
            new AnimationFrame(.2f, () -> setSrc("img\\obj\\obstacles\\campfire-3.png")),
            new AnimationFrame(.2f, () -> setSrc("img\\obj\\obstacles\\campfire-4.png"))
    };
    private  final AnimationFrame[] grazingCow = new AnimationFrame[]{
            new AnimationFrame(.6f, () -> setSrc("img\\obj\\obstacles\\cow-grazing-1.png")),
            new AnimationFrame(.2f, () -> setSrc("img\\obj\\obstacles\\cow-grazing-2.png"))
    };

    Animator animator = new Animator();

    protected void load() {
        super.load();
        //Festlegen Bild und Collider

        //Overworld Obstacles
        if (PlayMode.getInstance().getScene() == Scene.OVERWORLD) {


            id = (int) (Math.random() * 5);

            if (id >= 4) {
                addComponent(animator);
                animator.setFrames(campfire);
                setGlobalPosition(getParent().getGlobalPosition().x + (float) Math.random() * (getParent().getWidth() - getWidth()), 4f);

                Collider collider = new Collider();
                collider.collide.subscribe(this::onCollide);
                addComponent(collider);
                collider.setPadding(8f / 32f, 8f / 32f, 8f / 32f, 0f);
            }
            if (id == 3) {
                if (Math.random() < .5) {
                    setSrc("img\\obj\\obstacles\\cow.png");
                }
                else
                {
                    addComponent(animator);
                    animator.setFrames(grazingCow);
                }
                setGlobalPosition(getParent().getGlobalPosition().x + (float) Math.random() * (getParent().getWidth() - getWidth()), 4f+1f/32f);

                Collider collider = new Collider();
                collider.collide.subscribe(this::onCollide);
                addComponent(collider);
                collider.setPadding(3f / 32f, 14f / 32f, 5f / 32f, 1f/32f);
            }
            if (id == 2) {
                setSrc("img\\obj\\obstacles\\bush-1.png");
                setGlobalPosition(getParent().getGlobalPosition().x + (float) Math.random() * (getParent().getWidth() - getWidth()), 4f);

                Collider collider = new Collider();
                collider.collide.subscribe(this::onCollide);
                addComponent(collider);
                collider.setPadding(5f / 32f, 19f / 32f, 9f / 32f, 0f);
            }
            if (id == 1) {
                setSrc("img\\obj\\obstacles\\bush-2.png");
                setGlobalPosition(getParent().getGlobalPosition().x + (float) Math.random() * (getParent().getWidth() - getWidth()), 4f);

                Collider collider = new Collider();
                collider.collide.subscribe(this::onCollide);
                addComponent(collider);
                collider.setPadding(4f / 32f, 19f / 32f, 9f / 32f, 0f);
            }
            if (id == 0) {
                setSrc("img\\obj\\obstacles\\tree-1.png");
                setSize(2f, 2f);
                setGlobalPosition(getParent().getGlobalPosition().x + (float) Math.random() * (getParent().getWidth() - getWidth()), 3f);

                Collider collider = new Collider();
                collider.collide.subscribe(this::onCollide);
                addComponent(collider);
                collider.setPadding(10f / 32f, 1f / 32f, 7f / 32f, 1f);
            }
        }
        else {
            //Dark World Obstacles
            setSrc("img\\obj\\obstacles\\spears.png");
            setSize(1f, 2f);
            setGlobalPosition(getParent().getGlobalPosition().x + (float) Math.random() * (getParent().getWidth() - getWidth()), 3f);

            Collider collider = new Collider();
            collider.collide.subscribe(this::onCollide);
            addComponent(collider);
            collider.setPadding(10f/32f, 28f/32f, 21f/32f, 0f);
        }


    }

    @Override
    protected void update() {
        super.update();

    }

    private void onCollide(Collider other, Collision collision) {
        //Wenn Müa draufspringt Müa tot
        if (other.getOwner() == PlayMode.getInstance().mua && ( PlayMode.getInstance().getScene() == Scene.UNDERWORLD || PlayMode.getInstance().getScene() == Scene.OVERWORLD && id >= 4)) {
            PlayMode.getInstance().gameOver(true, false);
        }
    }
}