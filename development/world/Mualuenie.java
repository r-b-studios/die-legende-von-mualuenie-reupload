package development.world;

import development.data.DataFile;
import development.enums.Skin;
import engine.main.*;
import engine.enums.Collision;
import engine.tools.AnimationFrame;
import engine.tools.SafeList;
import engine.tools.WaveAudio;

import java.awt.event.KeyEvent;

public class Mualuenie extends ImageObject {

    private enum State {
        GROUND, JUMP, AIR
    }

    private final float GRAVITY = 10f;
    private final float JUMPFORCE = 7f;
    private final float YIHAA_PROBABILITY = 0.2f;
    private float currentJumpSpeed = 0f;
    private float airtime = 0;
    private State state = State.GROUND;
    private Skin skin;
    private Animator animator;

    private final float[] RUN_DELAYS = new float[] { .1f, .1f, .1f, .1f, .1f, .1f };
    private final float[] JUMP_DELAYS = new float[] { .05f, .05f, .05f, .1f };
    private final float AIR_DELAY = .1f;

    @Override
    protected void load() {
        super.load();

        //setzen des colliders
        setGlobalPosition(1, getCanvasSize().height - 2);

        Collider collider = new Collider();
        collider.collide.subscribe(this::onCollide);
        addComponent(collider);
        collider.setPadding(3/32f, 9/32f, 3/32f, 9/32f);

        animator = new Animator();
        addComponent(animator);

        skin = DataFile.getMuaSkin();

        setLayer(9);

        setRunAnimation();
    }

    @Override
    protected void update() {
        super.update();

        //Übergang Boden -> Fallen
        if (state == State.GROUND && airtime > 5f / getFPS()) {
            state = State.AIR;
            currentJumpSpeed = 0;

            setAirAnimation();
        }

        //Übergang Springen -> Fallen
        if (state == State.JUMP && airtime > .3f) {
            state = State.AIR;

            setAirAnimation();
        }

        //Springen
        if (getInput().keyPressed(KeyEvent.VK_SPACE, KeyEvent.VK_W) && state == State.GROUND) {
            jump();
        }
        //Horizontale Bewegung
        if (getInput().keyPressed(KeyEvent.VK_D) && getGlobalPosition().x < getCanvasSize().width - getWidth()) {
            move(5 / getFPS(),0);
        }
        if (getInput().keyPressed(KeyEvent.VK_A) && getGlobalPosition().x > 0f) {
            move(-5 / getFPS(),0);
        }

        if (state != State.GROUND) {
            //Schwerkraft
            move(0, currentJumpSpeed / getFPS());
            currentJumpSpeed += GRAVITY / getFPS();
        }

        airtime = airtime + 1f / getFPS();

        //aus der welt fallen
        if (getGlobalPosition().y > 5) {
            PlayMode.getInstance().gameOver(true, false);
        }
    }

    private void setRunAnimation() {
        setAnimation("run", RUN_DELAYS);
    }

    private void setJumpAnimation() {
        setAnimation("jump", JUMP_DELAYS);
    }

    private void setAirAnimation() {
        animator.setFrames(new AnimationFrame(AIR_DELAY, () -> setSrc("img\\obj\\mua\\" + skin.name().toLowerCase() + "\\jump-4.png")));
    }

    private void setAnimation(String type, float[] delays) {
        SafeList<AnimationFrame> result = new SafeList<>();

        for (int i = 0; i < delays.length; i++) {
            String src = "img\\obj\\mua\\" + skin.name().toLowerCase() + "\\" + type + "-" + (i + 1) + ".png";

            result.add(new AnimationFrame(delays[i], () -> {
                setSrc(src);
            }));
        }

        animator.setFrames(result.toArray(new AnimationFrame[0]));
    }

    //springen
    public void jump() {
        state = State.JUMP;
        currentJumpSpeed = -JUMPFORCE;

        setJumpAnimation();

        // Spielt manchmal ein "Yihaa"-Ruf ab
        if (Math.random() < YIHAA_PROBABILITY) {
            WaveAudio yihaa = new WaveAudio("audio\\yihaa.wav");
            yihaa.setVolume(DataFile.getSFXVolume());
            yihaa.play(false);
        }
    }


    //Kollidieren mit Boden
    private void onCollide(Collider other, Collision collision) {

        if (collision == Collision.VERTICAL && !(other.getOwner() instanceof Coin)) {
            airtime = 0;
            state = State.GROUND;
            setRunAnimation();
            setGlobalPosition(getGlobalPosition().x, other.getOwner().getGlobalPosition().y + other.getPadding().top() - getSize().height + 9f / 32f);
        } else {
            if (!(other.getOwner() instanceof Coin)) {
                PlayMode.getInstance().gameOver(true, false);
            }
        }
    }

}