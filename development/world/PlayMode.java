package development.world;

import development.enums.Scene;
import development.data.DataFile;
import development.data.Database;
import development.ui.GameOverMenu;
import development.ui.MainMenu;
import development.ui.PauseMenu;
import development.ui.simplified.MenuButton;
import engine.main.GameObject;
import engine.main.ImageObject;
import engine.main.Label;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Objekt, indem die "Action" abläuft
public class PlayMode extends GameObject {

    private static PlayMode instance;
    private final Scene scene;
    public Mualuenie mua;
    public int coins;
    public float score;
    private final Label scoreLabel = new Label();
    private final Label coinsLabel = new Label();

    public PlayMode(Scene scene) {
        this.scene = scene;
    }

    @Override
    protected void load() {
        super.load();

        instance = this;
        coins = 0;
        score = 0;

        //Hintergrund
        if (scene == Scene.OVERWORLD) setCanvasBackground(new Color(0xBEF9FF));
        else setCanvasBackground(new Color(0x45476c ));

        Color textColor = Color.white;

        if (scene == Scene.OVERWORLD) textColor = Color.black;

        // Weltgenerator
        Start start;
        addChildren(start = new Start());

        //scoreboard
        addChildren(scoreLabel);
        scoreLabel.setGlobalPosition((getCanvasSize().width - getWidth())/2f,0f);
        scoreLabel.setFont(getFont("font\\pixel.ttf").deriveFont(25f));
        scoreLabel.setColor(textColor);

        // Müaluenie
        mua = new Mualuenie();
        addChildren(mua);

        addChildren(new MenuButton("Pause", this::pause, getCanvasSize().width - 2, 0.5f));

        ImageObject coinIcon = new ImageObject();
        addChildren(coinIcon);
        coinIcon.setSrc("img\\obj\\mua_coin.png");
        coinIcon.setSize(.3f, .3f);
        coinIcon.setPosition(.2f, .2f);

        addChildren(coinsLabel);
        coinsLabel.setPosition(.3f, -.12f);
        coinsLabel.setFont(getFont("font\\pixel.ttf").deriveFont(15f));
        coinsLabel.setColor(textColor);
    }

    @Override
    protected void update() {
        super.update();

        //erhöhung des scores
        score += 100f / getFPS();

        scoreLabel.setText("Score: " + (int) score);
        coinsLabel.setText(String.valueOf(coins));
    }

    public Scene getScene() {
        return scene;
    }

    public void gameOver(boolean showGameOverScreen, boolean executeAlways)
    {
        if (getActive() || executeAlways) {
            if (DataFile.getHighscore() < score) {
                //setzen des Highscores in der Datenbank
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                Database.setHighscore((int) score, dtf.format(now));

                // lokales Speichern des Highscores
                DataFile.setHighscore((int) score);
            }

            DataFile.addCoins(coins);

            if (showGameOverScreen) {
                add(new GameOverMenu());
                setActive(false);

            } else {
                add(new MainMenu());
                destroy();
            }
        }
    }

    private void pause() {
        add(new PauseMenu());
        setActive(false);
    }

    public static PlayMode getInstance() {
        return instance;
    }
}