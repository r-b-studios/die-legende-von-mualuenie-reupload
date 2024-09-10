package development.ui;

import development.ui.simplified.MenuButton;
import development.world.PlayMode;
import engine.main.RectObject;
import java.awt.*;

// wird beim Game-Over angezeigt
public class GameOverMenu extends RectObject {

    @Override
    protected void load() {
        super.load();

        setSize(getCanvasSize());
        setColor(new Color(0x7C000000, true));

        addChildren(new MenuButton("Nochmal", this::tryAgain, 2));
        addChildren(new MenuButton("Hauptmenü", this::returnToMainMenu, 3));
    }

    // Startet die Runde von vorne
    private void tryAgain() {
        PlayMode.getInstance().destroy();
        replace(new PlayMode(PlayMode.getInstance().getScene()));
    }

    // führt zum MainMenu zurück
    private void returnToMainMenu() {
        PlayMode.getInstance().destroy();
        replace(new MainMenu());
    }
}