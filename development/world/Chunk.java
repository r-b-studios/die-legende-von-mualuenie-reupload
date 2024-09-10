package development.world;

import development.enums.Scene;
import development.obstacles.Obstacle;
import development.obstacles.ObstacleBird;
import development.obstacles.ObstacleGraves;
import engine.main.Collider;
import engine.main.GameObject;
import engine.main.ImageObject;
import engine.tools.SafeList;

// Teil der Welt der sich horizontal nach links bewegt
public class Chunk extends ImageObject {

    boolean duplicated = false;
    int id = 0;

    SafeList<GameObject> obstacles = new SafeList<>();

    @Override
    protected void load() {
        super.load();
        //zufälliges setzen des Bodens
        if (PlayMode.getInstance().getScene() == Scene.OVERWORLD) {
            //Overworld Böden
            id = (int) (Math.random() * 8);
            if (id == 0)
            {
                setSrc("img\\obj\\world\\ow\\ow-large-hole.png");
                setGlobalPosition(12.5f, 5f);
                setSize(12.5f, 2f);
                Collider colliderL = new Collider();
                addComponent(colliderL);
                colliderL.setPadding(0f,0f,10f,0f);
                Collider colliderR = new Collider();
                addComponent(colliderR);
                colliderR.setPadding(10f,0f,0f,0f);
            }
            if (id == 1)
            {
                setSrc("img\\obj\\world\\ow\\ow-hole.png");
                setGlobalPosition(12.5f, 5f);
                setSize(2.5f, 2f);
                Collider colliderL = new Collider();
                addComponent(colliderL);
                colliderL.setPadding(0f,0f,2.25f,0f);
                Collider colliderR = new Collider();
                addComponent(colliderR);
                colliderR.setPadding(2.25f,0f,0f,0f);
            }
            if (id == 2)
            {
                setSrc("img\\obj\\world\\ow\\ow-bridge.png");
                setGlobalPosition(12.5f, 4f);
                setSize(5f, 3f);
                Collider collider = new Collider();
                addComponent(collider);
                collider.setPadding(0f,1f,0f,0f);
            }
            if (id == 3)
            {
                setSrc("img\\obj\\world\\ow\\ow-rocks.png");
                setGlobalPosition(12.5f, 3.5f);
                setSize(2.5f, 3.5f);
                Collider collider = new Collider();
                addComponent(collider);
                collider.setPadding(10f/32f,5f/32f,0f,0f);
            }
            if (id >= 4) {
                setSrc("img\\obj\\world\\ow\\ow-chunk.png");
                setGlobalPosition(12.5f, 5f);
                setSize(2.5f, 2f);
                Collider collider = new Collider();
                addComponent(collider);
            }
        }
        else {
            //Dark World Böden
        id = (int) (Math.random() * 4);
        if (id == 0) {

            setSrc("img\\obj\\world\\dark-chunk\\dark-chunk.png");
            setGlobalPosition(12.5f, 5f);
            setSize(2.5f, 2f);
            Collider collider = new Collider();
            addComponent(collider);
        }
        if (id == 1) {
            setSrc("img\\obj\\world\\dark-chunk\\dark-clock-tower.png");
            setGlobalPosition(12.5f, 1.5f);
            setSize(2f, 5.5f);
            Collider colliderRoof = new Collider();
            addComponent(colliderRoof);
            colliderRoof.setPadding(8f / 64f * 2f, 10f / 176f * 5.5f, 8f / 64f * 2f, 175f / 176f * 5.5f);
            Collider colliderFloor = new Collider();
            addComponent(colliderFloor);
            colliderFloor.setPadding(0f, 112f / 176f * 5.5f, 0f, 0f);


        }
        if (id == 2) {
            setSrc("img\\obj\\world\\dark-chunk\\dark-chunk-hole.png");
            setGlobalPosition(12.5f, 5f);
            setSize(2.5f, 2f);

            Collider colliderL = new Collider();
            addComponent(colliderL);
            colliderL.setPadding(0f, 0f, 63f / 80f * 2.5f, 58 / 64f * 2f);
            Collider colliderR = new Collider();
            addComponent(colliderR);
            colliderR.setPadding(63f / 80f * 2.5f, 0f, 0f, 58 / 64f * 2f);

            if (Math.random() > .5) {
                Platform platform = new Platform();
                addChildren(platform);
            }
        }
        if (id >= 3) {
            setSrc("img\\obj\\world\\dark-chunk\\dark-graveyard.png");
            setGlobalPosition(12.5f, 5f);
            setSize(2.5f, 2f);
            Collider collider = new Collider();
            addComponent(collider);

            ObstacleGraves graves = new ObstacleGraves();
            addChildren(graves);
        }
    }

        //Zufällige Hindernisse
        ObstacleBird obstacleBird = new ObstacleBird();
        obstacles.add(obstacleBird);
        Obstacle obstacle = new Obstacle();
        obstacles.add(obstacle);

        if (Math.random() > .3 && ((PlayMode.getInstance().getScene() == Scene.OVERWORLD && id > 3) || (PlayMode.getInstance().getScene() == Scene.UNDERWORLD && id != 2)))
        {
            addChildren(obstacles.get((int) (Math.random() * obstacles.size())));
        }

        Coin coin = new Coin();
        addChildren(coin);


    }

    @Override
    protected void update() {
        super.update();
        move((-2 - 0.0001f * PlayMode.getInstance().score)/ getFPS(), 0);

        //löschen, falls außerhalb des bildschirms
        if (getGlobalPosition().x <= -getWidth()) {
            destroy();
        }

        //generieren neuer chunks, wenn vollkommen im bildschirm
        if (!duplicated && getGlobalPosition().x <= getCanvasSize().width - getWidth())
        {
            Chunk chunk;
            getParent().addChildren(chunk= new Chunk());
            chunk.setGlobalPosition(getGlobalPosition().x + getWidth(), chunk.getY());
            duplicated = true;
        }

    }

}
